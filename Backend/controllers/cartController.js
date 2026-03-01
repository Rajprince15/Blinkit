const Database = require('../models/db');

/**
 * Get all cart items for the logged in user
 */
const getCart = async (req, res) => {
  try {
    const userId = req.user.id;

    // Get cart items with full product details
    const rawItems = await Database.query(
      `SELECT ci.id as cart_item_id, ci.product_id, ci.quantity, ci.added_at, ci.updated_at,
              p.id as p_id, p.category_id, p.name, p.description, p.brand, p.image_url, 
              p.price, p.original_price, p.discount_percentage, p.unit, p.stock_quantity,
              p.rating, p.total_reviews, p.is_featured, p.is_available,
              (ci.quantity * p.price) as item_total
       FROM cart_items ci
       JOIN products p ON ci.product_id = p.id
       WHERE ci.user_id = ?`,
      [userId]
    );

    // Restructure to match Android CartItem model with nested Product
    const items = rawItems.map(item => ({
      id: item.cart_item_id,
      user_id: userId,
      product_id: item.product_id,
      quantity: item.quantity,
      added_at: item.added_at,
      updated_at: item.updated_at,
      product: {
        id: item.p_id,
        category_id: item.category_id,
        name: item.name,
        description: item.description,
        brand: item.brand,
        image_url: item.image_url,
        price: parseFloat(item.price),
        original_price: item.original_price ? parseFloat(item.original_price) : null,
        discount_percentage: item.discount_percentage,
        unit: item.unit,
        stock_quantity: item.stock_quantity,
        rating: item.rating ? parseFloat(item.rating) : 0.0,
        total_reviews: item.total_reviews,
        is_featured: item.is_featured,
        is_available: item.is_available
      }
    }));

    // Compute summary
    const summary = {
      item_count: items.reduce((sum, item) => sum + item.quantity, 0),
      subtotal: items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0),
      delivery_fee: 0.0,
      discount_amount: 0.0,
      total_amount: 0.0,
      items: items
    };
    summary.total_amount = summary.subtotal + summary.delivery_fee - summary.discount_amount;

    
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
