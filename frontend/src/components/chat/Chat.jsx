import { useState } from "react"

const Chat = ({ messages, onSend }) => {
  const [messageContent, setMessageContent] = useState("")

  return (
    <div className="chat">
      <div className="chat__messages">
        {messages.map((message) => (
          <div className={`chat__messages__message ${message.sender}`}>
            <div className="chat__messages__message__icon">
              {message.sender.substring(0, 1)}
            </div>
            <div className="chat__messages__message__content">
              <div className="">{message.content}</div>
              <div className="chat__messageschat__messages__message__status">
                {message.seen}
              </div>
            </div>
          </div>
        ))}
      </div>
      <div className="chat__input">
        <label className="chat__input__message">
          Message:
          <input
            type="text"
            value={messageContent}
            onChange={(e) => setMessageContent(e.target.value)}
          />
        </label>

        <button
          className="chat__input__submit"
          onClick={() => {
            onSend(messageContent)
            setMessageContent("")
          }}
        >
          Send
        </button>
      </div>
    </div>
  )
}

export default Chat
