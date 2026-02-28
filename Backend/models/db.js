const { promisePool } = require('../config/database');

/**
 * Database helper functions for common operations
 */

class Database {
  // Execute query with parameters
  static async query(sql, params = []) {
    try {
      const [results] = await promisePool.execute(sql, params);
      return results;
    } catch (error) {
      console.error('Database query error:', error);
      throw error;
    }
  }

  // Get single row
  static async getOne(sql, params = []) {
    try {
      const [results] = await promisePool.execute(sql, params);
      return results[0] || null;
    } catch (error) {
      console.error('Database getOne error:', error);
      throw error;
    }
  }

  // Get all rows
  static async getAll(sql, params = []) {
    try {
      const [results] = await promisePool.execute(sql, params);
      return results;
    } catch (error) {
      console.error('Database getAll error:', error);
      throw error;
    }
  }

  // Insert and return inserted ID
  static async insert(sql, params = []) {
    try {
      const [result] = await promisePool.execute(sql, params);
      return result.insertId;
    } catch (error) {
      console.error('Database insert error:', error);
      throw error;
    }
  }

  // Update and return affected rows
  static async update(sql, params = []) {
    try {
      const [result] = await promisePool.execute(sql, params);
      return result.affectedRows;
    } catch (error) {
      console.error('Database update error:', error);
      throw error;
    }
  }

  // Delete and return affected rows
  static async delete(sql, params = []) {
    try {
      const [result] = await promisePool.execute(sql, params);
      return result.affectedRows;
    } catch (error) {
      console.error('Database delete error:', error);
      throw error;
    }
  }
}

module.exports = Database;
