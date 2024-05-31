import { expect } from 'chai';
import sinon from 'sinon';
import chai from 'chai';
import express from "express";
import routers from "../../routers";
import bodyParser from 'body-parser';
import { afterEach } from "mocha";
import chaiHttp from "chai-http";
import Message from "../../model/message";
import { MongoMemoryServer } from "mongodb-memory-server";
import mongoose from "mongoose";
import axios from 'axios';
import dotenv from "dotenv";

chai.use(chaiHttp);
chai.should();

const sandbox = sinon.createSandbox();

const app = express();

app.use(bodyParser.json({ limit: '1mb' }));
app.use('/', routers);
dotenv.config();

let mongoServer: MongoMemoryServer;
let axiosGetStub: sinon.SinonStub;

describe('Message Controller', () => {
    before(async () => {
        mongoServer = await MongoMemoryServer.create();
        const mongoUri = mongoServer.getUri();
        await mongoose.connect(mongoUri);
    });

    beforeEach(async () => {
        await Message.deleteMany({});
        axiosGetStub = sandbox.stub(axios, 'get');  // Stub axios.get before each test
    });

    afterEach(() => {
        sandbox.restore();
    });

    after(async () => {
        await mongoose.disconnect();
        await mongoServer.stop();
    });

    it('should create a new message and return 201 status', (done) => {
        const message = {
            userId: 1,
            content: 'Test message'
        };

        axiosGetStub.withArgs(`${process.env.USER_SERVICE_BASE_URL}/users/${message.userId}`)
            .returns(Promise.resolve({ status: 200, data: { exists: true } }));

        chai.request(app)
            .post('/api/v1/messages')
            .send(message)
            .end((err, res) => {
                expect(err).to.be.null;
                expect(res).to.have.status(201);
                expect(res.body).to.have.property('id');
                done();
            });
    });

    it('should list the messages by user id', (done) => {
        const userId = 1;

        const message = {
            userId: 1,
            content: 'Test message'
        };

        axiosGetStub.withArgs(`${process.env.USER_SERVICE_BASE_URL}/users/${message.userId}`)
            .returns(Promise.resolve({ status: 200, data: { exists: true } }));

        chai.request(app)
            .post('/api/v1/messages')
            .send(message)
            .end(() => {
                chai.request(app)
                    .get('/api/v1/messages')
                    .send({ userId, from: 0, size: 10 })
                    .end((_, res) => {
                        res.should.have.status(200);
                        expect(res.body).to.be.an('array');
                        done();
                    });
            });
    });

    it('should count messages for given user ids', (done) => {
        const userIds = [1];

        chai.request(app)
            .post('/api/v1/messages/_counts')
            .send({ userIds })
            .end((_, res) => {
                res.should.have.status(200);
                expect(res.body).to.be.an('object');
                expect(res.body[`id${userIds[0]}`]).to.exist;
                done();
            });
    });

    it('should return 400 if no userId is provided when creating a message', (done) => {
        const message = {
            content: 'Test message'
        };

        chai.request(app)
            .post('/api/v1/messages')
            .send(message)
            .end((_, res) => {
                res.should.have.status(400);
                done();
            });
    });

    it('should return 500 if no content is provided when creating a message', (done) => {
        const message = {
            userId: 1
        };

        axiosGetStub.withArgs(`${process.env.USER_SERVICE_BASE_URL}/users/${message.userId}`)
            .returns(Promise.resolve({ status: 200, data: { exists: true } }));

        chai.request(app)
            .post('/api/v1/messages')
            .send(message)
            .end((_, res) => {
                res.should.have.status(500);
                done();
            });
    });

    it('should return 400 if no userId is provided when listing messages', (done) => {
        chai.request(app)
            .get('/api/v1/messages')
            .send({ from: 0, size: 10 })
            .end((_, res) => {
                res.should.have.status(400);
                done();
            });
    });

    it('should return 400 if no userIds are provided when counting messages', (done) => {
        chai.request(app)
            .post('/api/v1/messages/_counts')
            .send({})
            .end((_, res) => {
                res.should.have.status(400);
                done();
            });
    });
});
