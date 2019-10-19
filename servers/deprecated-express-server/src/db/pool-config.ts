import { Pool } from 'pg';
import logger from '../server/logger';

// TODO - externalize into env vars
// https://node-postgres.com/features/connecting

const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'postgres',
  password: 'qz101022'
});

pool.on('error', (err, client) => {
  logger.error('SQL Error on idle client', err);
  // TODO - should we? Or destroy all clients?
  process.exit(-1);
});

export const getPool = () => {
  return pool;
};

