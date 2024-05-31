import express from 'express';
import {
  createMessage,
  listMessages,
  countMessages,
} from 'src/controllers/messages';

const router = express.Router();

router.post('/api/v1/messages', createMessage);
router.get('/api/v1/messages', listMessages);
router.post('/api/v1/messages/_counts', countMessages);

export default router;