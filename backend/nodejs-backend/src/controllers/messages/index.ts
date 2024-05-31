import {Request, Response} from 'express';
import {
    createMessage as createMessageService,
    listMessages as listMessagesService,
    countMessages as countMessagesService, checkUserExists,
} from 'src/services/message';
import {MessageSaveDto} from 'src/dto/message/messageSaveDto';
import log4js from "log4js";
import httpStatus from "http-status";

const logger = log4js.getLogger('messagesController');

/**
 * Create a new message
 * @param {Request} req - Express request object
 * @param {Response} res - Express response object
 * @returns {Promise<Response>} - Express response object
 */
export const createMessage = async (req: Request, res: Response): Promise<Response> => {
    try {
        const message = new MessageSaveDto(req.body);

        // Check if the user exists
        const userExists = await checkUserExists(message.userId);
        if (!userExists) {
            return res.status(httpStatus.BAD_REQUEST).send({ message: 'User does not exist' });
        }

        // Create message
        const id = await createMessageService({ ...message });
        return res.status(httpStatus.CREATED).send({ id });
    } catch (err) {
        const error = err as Error;
        logger.error('Error in creating message record', error);

        // Return detailed error messages
        return res.status(httpStatus.INTERNAL_SERVER_ERROR).send({ message: error.message });
    }
};

/**
 * List messages for a user
 * @param {Request} req - Express request object
 * @param {Response} res - Express response object
 * @returns {Promise<Response>} - Express response object
 */
export const listMessages = async (req: Request, res: Response): Promise<Response> => {
    const userId = Number(req.body.userId);
    const size = Number(req.body.size) || 10;
    const from = Number(req.body.from) || 0;

    if (!userId) {
        return res.status(httpStatus.BAD_REQUEST).send({ message: 'userId is required' });
    }

    try {
        const result = await listMessagesService(userId, size, from);
        if (result.length === 0) {
            return res.status(httpStatus.NOT_FOUND).send({ message: 'No messages found for the given userId' });
        }
        return res.status(httpStatus.OK).send(result);
    } catch (err) {
        const error = err as Error;
        logger.error(`Error in retrieving messages for user with id ${userId}.`, error);

        return res.status(httpStatus.INTERNAL_SERVER_ERROR).send({ message: error.message });
    }
};

/**
 * Count messages for a list of users
 * @param {Request} req - Express request object
 * @param {Response} res - Express response object
 * @returns {Promise<Response>} - Express response object
 */
export const countMessages = async (req: Request, res: Response): Promise<Response> => {
    const userIds: number[] = req.body.userIds;
    if (!userIds || !userIds.length) {
        return res.status(httpStatus.BAD_REQUEST).send({ message: 'userIds is required' });
    }

    try {
        const countsArray = await countMessagesService(userIds);
        const result = countsArray.reduce((acc, { id, count }) => ({ ...acc, [id]: count }), {});
        return res.status(httpStatus.OK).send(result);
    } catch (err) {
        const error = err as Error;
        logger.error('Error in counting messages.', error);
        return res.status(httpStatus.INTERNAL_SERVER_ERROR).send({ message: error.message });
    }
};