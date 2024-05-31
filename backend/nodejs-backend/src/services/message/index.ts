import Message, {IMessage} from 'src/model/message';
import {MessageSaveDto} from 'src/dto/message/messageSaveDto';
import axios from 'axios';
import log4js from "log4js";
import dotenv from 'dotenv';

dotenv.config();

const logger = log4js.getLogger('messageService');

/**
 * Check if a user exists
 * @param {number} userId - The ID of the user
 * @returns {Promise<boolean>} - A promise that resolves to a boolean indicating if the user exists
 */
export const checkUserExists = async (userId: number): Promise<boolean> => {
  try {
    const userResponse = await axios.get(`${process.env.USER_SERVICE_BASE_URL}/users/${userId}`);
    return userResponse.status === 200 && !!userResponse.data;
  } catch (error) {
    logger.error('Error checking if user exists:', error);
    return false;
  }
};

/**
 * Create a new message
 * @param {MessageSaveDto} messageDto - The message data transfer object
 * @returns {Promise<unknown>} - A promise that resolves to the ID of the created message
 */
export const createMessage = async (messageDto: MessageSaveDto): Promise<unknown> => {
  try {
    if (!messageDto.content || messageDto.content.trim() === '') {
      logger.error('Error creating message: content is required');
      return Promise.reject(new Error('Content is required'));
    }
    const message = await new Message(messageDto).save();
    return message._id;
  } catch (error) {
    logger.error('Error creating message:', error);
    throw error;
  }
};

/**
 * List messages for a user
 * @param {number} userId - The ID of the user
 * @param {number} size - The number of messages to return
 * @param {number} from - The number of messages to skip
 * @returns {Promise<IMessage[]>} - A promise that resolves to an array of messages
 */
export const listMessages = async (userId: number, size: number, from: number): Promise<IMessage[]> => {
  try {
    return await Message.find({userId})
        .sort({timestamp: -1})
        .skip(from)
        .limit(size);
  } catch (error) {
    logger.error('Error listing messages:', error);
    throw error;
  }
};

/**
 * Count messages for a list of users
 * @param {number[]} userIds - An array of user IDs
 * @returns {Promise<{ id: string, count: number }[]>} - A promise that resolves to an array of objects, each containing a user ID and a message count
 */
export const countMessages = async (userIds: number[]): Promise<{ id: string, count: number }[]> => {
  const counts: { id: string, count: number }[] = [];
  try {
    for (const userId of userIds) {
      const count = await Message.countDocuments({ userId });
      counts.push({ id: `id${userId}`, count });
    }
    return counts;
  } catch (error) {
    logger.error('Error counting messages:', error);
    throw error;
  }
};