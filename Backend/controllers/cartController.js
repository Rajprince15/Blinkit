const Database = require('../models/db');

/**
 * Get all cart items for the logged in user
 */
const getCart = async (req, res) => {
  try {
    const userId = req.user.id;

    const items = await Database.query(
      `SELECT ci.id, ci.product_id, ci.quantity,
              p.name as product_name, p.price, p.image_url,
              (ci.quantity * p.price) as total_price
       FROM cart_items ci
       JOIN products p ON ci.product_id = p.id
       WHERE ci.user_id = ?`,
      [userId]
    );

    // compute summary
    const summary = items.reduce(
      (acc, item) => {
        acc.totalItems += item.quantity;
        acc.subtotal += parseFloat(item.total_price);
        return acc;
      },
      { totalItems: 0, subtotal: 0.0 }
    );

    // optionally add delivery fee and discount here
    // snake_case fields for client
    summary.delivery_fee = 0.0;
    summary.discount_amount = 0.0;
    summary.total_amount = summary.subtotal + summary.delivery_fee - summary.discount_amount;

    // embed items list inside summary for client convenience
    summary.items = items;

    // convert totalItems -> item_count for compatibility
    summary.item_count = summary.totalItems;
    delete summary.totalItems;
    res.status(200).json({
      success: true,
      data: summary
    });
  } catch (error) {
    console.error('Get cart error:', error);
    res.status(500).json({ success: false, message: 'Server error fetching cart' });
  }
};

/**
 * Add product to cart (or update quantity if exists)
 */
const addToCart = async (req, res) => {
  try {
    const userId = req.user.id;
    // support both camelCase and snake_case from client
    const productId = req.body.productId || req.body.product_id;
    const quantity = req.body.quantity || req.body.qty || req.body.q || 0;

    if (!productId || !quantity || quantity < 1) {
      return res.status(400).json({ success: false, message: 'Product ID and positive quantity required' });
    }

    // check if item exists
    const existing = await Database.getOne(
      'SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?',
      [userId, productId]
    );

    if (existing) {
      // update quantity
      const newQty = existing.quantity + quantity;
      await Database.update(
        'UPDATE cart_items SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?',
        [newQty, existing.id]
      );
      return res.status(200).json({ success: true, message: 'Cart item quantity updated', cartItemId: existing.id });
    } else {
      const insertId = await Database.insert(
        'INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)',
        [userId, productId, quantity]
      );
      return res.status(201).json({ success: true, message: 'Product added to cart', cartItemId: insertId });
    }
  } catch (error) {
    console.error('Add to cart error:', error);
    res.status(500).json({ success: false, message: 'Server error adding to cart' });
  }
};

/**
 * Update quantity for a cart item
 */
const updateCartItem = async (req, res) => {
  try {
    const userId = req.user.id;
    const { cartItemId } = req.params;
    const quantity = req.body.quantity || req.body.qty || 0;

    if (!quantity || quantity < 1) {
      return res.status(400).json({ success: false, message: 'Valid quantity required' });
    }

    // ensure the item belongs to user
    const item = await Database.getOne(
      'SELECT id FROM cart_items WHERE id = ? AND user_id = ?',
      [cartItemId, userId]
    );
    if (!item) {
      return res.status(404).json({ success: false, message: 'Cart item not found' });
    }

    await Database.update(
      'UPDATE cart_items SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?',
      [quantity, cartItemId]
    );

    res.status(200).json({ success: true, message: 'Cart item updated' });
  } catch (error) {
    console.error('Update cart item error:', error);
    res.status(500).json({ success: false, message: 'Server error updating cart item' });
  }
};

/**
 * Remove a single cart item
 */
const removeCartItem = async (req, res) => {
  try {
    const userId = req.user.id;
    const { cartItemId } = req.params;

    const deleted = await Database.delete(
      'DELETE FROM cart_items WHERE id = ? AND user_id = ?',
      [cartItemId, userId]
    );
    if (deleted === 0) {
      return res.status(404).json({ success: false, message: 'Cart item not found' });
    }

    res.status(200).json({ success: true, message: 'Cart item removed' });
  } catch (error) {
    console.error('Remove cart item error:', error);
    res.status(500).json({ success: false, message: 'Server error removing cart item' });
  }
};

/**
 * Clear the user's cart
 */
const clearCart = async (req, res) => {
  try {
    const userId = req.user.id;
    await Database.delete('DELETE FROM cart_items WHERE user_id = ?', [userId]);
    res.status(200).json({ success: true, message: 'Cart cleared' });
  } catch (error) {
    console.error('Clear cart error:', error);
    res.status(500).json({ success: false, message: 'Server error clearing cart' });
  }
};

module.exports = {
  getCart,
  addToCart,
  updateCartItem,
  removeCartItem,
  clearCart
};
