// liberally lifted from winston docs with modifications to syntax and loggers
// see https://www.npmjs.com/package/winston#formats

import winston from 'winston';
const { format } = winston;

const logger = winston.createLogger({
  level: 'silly',
  format: format.combine(
      format.timestamp(),
      format.simple(),
  ),
  transports: [
    new winston.transports.Console()
  ],
  exitOnError: false
});
export default logger;
