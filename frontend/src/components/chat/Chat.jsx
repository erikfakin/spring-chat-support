import { useState } from "react";
import Message from "./Message";

const Chat = ({ messages, onSend }) => {
    const [messageContent, setMessageContent] = useState("")

    return (
        <div className="chat">
            <div className="chat__messages">
                {messages.map(message => {
                    <Message message={message} />
                })}
            </div>
            <div className="chat__input">
                <label className="chat__input__message">
                    Message:
                    <input type="text" value={messageContent} onChange={e => setMessageContent(e.target.value)} />
                </label>

                <button className="chat__input__submit" onClick={() => onSend(messageContent)}>Send</button>
            </div>
        </div>
    );
}

export default Chat;