import { expect } from 'chai';
import sinon from 'sinon';
import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';
import Message from '../../model/message';
import * as messageService from '../../services/message';
import { MessageSaveDto } from 'src/dto/message/messageSaveDto';

const sandbox = sinon.createSandbox();

let mongoServer: MongoMemoryServer;

describe('Message Service', () => {
    before(async () => {
        mongoServer = await MongoMemoryServer.create();
        const mongoUri = mongoServer.getUri();
        await mongoose.connect(mongoUri);
    });

    beforeEach(async () => {
        await Message.deleteMany({});
    });

    afterEach(() => {
        sandbox.restore();
    });

    after(async () => {
        await mongoose.disconnect();
        await mongoServer.stop();
    });

    it('createMessage should create a new message and return its id', async () => {
        const messageDto: MessageSaveDto = { userId: 1, content: 'Test message' };
        const messageId = await messageService.createMessage(messageDto);
        const message = await Message.findById(messageId);

        expect(message).to.exist;
        expect(message?.userId).to.equal(messageDto.userId);
        expect(message?.content).to.equal(messageDto.content);
    });

    it('listMessages should return messages for a valid userId', async () => {
        const message1 = new Message({ userId: 1, content: 'Test message 1' });
        const message2 = new Message({ userId: 1, content: 'Test message 2' });
        await message1.save();
        await message2.save();

        const messages = await messageService.listMessages(1, 10, 0);

        expect(messages.length).to.equal(2);
    });

    it('countMessages should return message counts for given userIds', async () => {
        const message1 = new Message({ userId: 1, content: 'Test message 1' });
        const message2 = new Message({ userId: 2, content: 'Test message 2' });
        await message1.save();
        await message2.save();

        const counts = await messageService.countMessages([1, 2]);

        expect(counts).to.have.length(2);
        expect(counts[0].count).to.equal(1);
        expect(counts[1].count).to.equal(1);
    });
});
