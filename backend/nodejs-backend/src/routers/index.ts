import express from 'express';
import ping from 'src/controllers/ping';

import messages from './messages';

const router = express.Router();

router.get('/ping', ping);

router.use('/', messages);

export default router;