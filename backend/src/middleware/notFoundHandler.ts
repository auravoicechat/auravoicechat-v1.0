/**
 * 404 Not Found Handler
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Request, Response } from 'express';

export const notFoundHandler = (req: Request, res: Response) => {
  res.status(404).json({
    error: {
      code: 'NOT_FOUND',
      message: `Route ${req.originalUrl} not found`
    }
  });
};
