import mongoose, {Document, Schema} from 'mongoose';

export interface IMessage extends Document {
    userId: number;
    content: string;
    timestamp?: Date;
}

const messageSchema = new Schema({
    userId: {type: Number, required: true},
    content: {type: String, required: true},
    timestamp: { type: Date, default: Date.now, index: true },
});
const Message = mongoose.model<IMessage>('Message', messageSchema);

export default Message;