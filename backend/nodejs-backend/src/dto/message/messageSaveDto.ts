export class MessageSaveDto {
    userId: number;
    content: string;
    timestamp?: Date;

    constructor({
                    userId,
                    content,
                    timestamp,
                }: {
        userId: number;
        content: string;
        timestamp?: Date;
    }) {
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
