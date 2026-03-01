const Database = require('../models/db');

/**
 * Get all addresses for user
 */
const getAddresses = async (req, res) => {
  try {
    const userId = req.user.id;
    const addresses = await Database.query(
      'SELECT * FROM addresses WHERE user_id = ? ORDER BY is_default DESC, id ASC',
      [userId]
    );
    res.status(200).json({ success: true, count: addresses.length, data: addresses });
  } catch (error) {
    console.error('Get addresses error:', error);
    res.status(500).json({ success: false, message: 'Server error fetching addresses' });
  }
};

/**
 * Add new address
 */
const addAddress = async (req, res) => {
  try {
    const userId = req.user.id;
    const fullName = req.body.fullName || req.body.full_name;
    const phone = req.body.phone;
    const addressLine1 = req.body.addressLine1 || req.body.address_line1;
    const addressLine2 = req.body.addressLine2 || req.body.address_line2;
    const city = req.body.city;
    const state = req.body.state;
    const pincode = req.body.pincode;
    const addressType = req.body.addressType || req.body.address_type;

    if (!fullName || !phone || !addressLine1 || !city || !state || !pincode) {
      return res.status(400).json({ success: false, message: 'Required fields missing' });
    }

    // if setting default, unset others
    let isDefault = req.body.isDefault || false;
    if (isDefault) {
      await Database.update(
        'UPDATE addresses SET is_default = FALSE WHERE user_id = ?',
        [userId]
      );
    }

    const insertId = await Database.insert(
      `INSERT INTO addresses
       (user_id, full_name, phone, address_line1, address_line2, city, state, pincode, address_type, is_default)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [userId, fullName, phone, addressLine1, addressLine2 || null, city, state, pincode, addressType || 'HOME', isDefault]
    );

    res.status(201).json({ success: true, message: 'Address added', addressId: insertId });
  } catch (error) {
    console.error('Add address error:', error);
    res.status(500).json({ success: false, message: 'Server error adding address' });
  }
};

/**
 * Update an existing address
 */
const updateAddress = async (req, res) => {
  try {
    const userId = req.user.id;
    const { addressId } = req.params;
    const fullName = req.body.fullName || req.body.full_name;
    const phone = req.body.phone;
    const addressLine1 = req.body.addressLine1 || req.body.address_line1;
    const addressLine2 = req.body.addressLine2 || req.body.address_line2;
    const city = req.body.city;
    const state = req.body.state;
    const pincode = req.body.pincode;
    const addressType = req.body.addressType || req.body.address_type;

    const address = await Database.getOne(
      'SELECT id FROM addresses WHERE id = ? AND user_id = ?',
      [addressId, userId]
    );
    if (!address) {
      return res.status(404).json({ success: false, message: 'Address not found' });
    }

    // handle default flag
    let isDefault = req.body.isDefault;
    if (isDefault) {
      await Database.update('UPDATE addresses SET is_default = FALSE WHERE user_id = ?', [userId]);
    }

    await Database.update(
      `UPDATE addresses SET full_name = ?, phone = ?, address_line1 = ?, address_line2 = ?, city = ?, state = ?, pincode = ?, address_type = ?, is_default = ? WHERE id = ?`,
      [fullName, phone, addressLine1, addressLine2 || null, city, state, pincode, addressType || 'HOME', isDefault || false, addressId]
    );

    res.status(200).json({ success: true, message: 'Address updated' });
  } catch (error) {
    console.error('Update address error:', error);
    res.status(500).json({ success: false, message: 'Server error updating address' });
  }
};

/**
 * Delete an address
 */
const deleteAddress = async (req, res) => {
  try {
    const userId = req.user.id;
    const { addressId } = req.params;

    const deleted = await Database.delete(
      'DELETE FROM addresses WHERE id = ? AND user_id = ?',
      [addressId, userId]
    );
    if (deleted === 0) {
      return res.status(404).json({ success: false, message: 'Address not found' });
    }

    res.status(200).json({ success: true, message: 'Address deleted' });
  } catch (error) {
    console.error('Delete address error:', error);
    res.status(500).json({ success: false, message: 'Server error deleting address' });
  }
};

/**
 * Set an address as default
 */
const setDefaultAddress = async (req, res) => {
  try {
    const userId = req.user.id;
    const { addressId } = req.params;

    const address = await Database.getOne(
      'SELECT id FROM addresses WHERE id = ? AND user_id = ?',
      [addressId, userId]
    );
    if (!address) {
      return res.status(404).json({ success: false, message: 'Address not found' });
    }

    await Database.update('UPDATE addresses SET is_default = FALSE WHERE user_id = ?', [userId]);
    await Database.update('UPDATE addresses SET is_default = TRUE WHERE id = ?', [addressId]);

    res.status(200).json({ success: true, message: 'Default address set' });
  } catch (error) {
    console.error('Set default address error:', error);
    res.status(500).json({ success: false, message: 'Server error setting default address' });
  }
};

module.exports = {
  getAddresses,
  addAddress,
  updateAddress,
  deleteAddress,
  setDefaultAddress
};
