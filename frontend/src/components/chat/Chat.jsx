import { useEffect, useRef, useState } from "react"
import { ReactComponent as SendIcon } from "../../static/icons/send.svg"
import { ReactComponent as SupportIcon } from "../../static/icons/support.svg"
import { ReactComponent as ClientIcon } from "../../static/icons/client.svg"
import { ReactComponent as SeenIcon } from "../../static/icons/seen.svg"

import "./Chat.scss"
import { formatDate } from "../../utils/date/dateUtils"

const Chat = ({ messages, onSend, user, room }) => {
  const [messageContent, setMessageContent] = useState("")
  const messagesBottomRef = useRef(null)

  useEffect(() => {
    messagesBottomRef.current.scrollIntoView({ behavior: "smooth" })
  }, [messages])

  const handleOnSend = () => {
    if (messageContent.trim() === "") {
      setMessageContent("")
      return
    }
    onSend(messageContent)
    setMessageContent("")
  }

  return (
    <div className={`chat chat${user}`}>
      <div className="chat__messages">
        {messages.length > 0 ? (
          messages.map((message) => (
            <div className={`chat__message ${message.sender}`} key={message.id}>
              <div className="chat__message__wrapper">
                <div className="chat__message__icon">
                  {message.sender === "support" ? (
                    <SupportIcon className="chat__message__icon__support" />
                  ) : (
                    <ClientIcon className="chat__message__icon__client" />
                  )}
                </div>
                <div className="chat__message__content">
                  <div className="">{message.content}</div>
                  <div className="chat__message__status">
                    {message.seen && (
                      <>
                        {formatDate(message.seen)}
                        <SeenIcon className="chat__message__status__seen" />
                      </>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>Send a message to start chatting.</p>
        )}
        <div className="chat__messages__bottom" ref={messagesBottomRef}></div>
      </div>
      <div className="chat__input">
        {room?.status === "OFFLINE" ? (
          <p>
            {`The user if offline. To continue the conversation please send an
            email to `}
            <a href={"mailto:" + room.clientUser.email}>
              {room.clientUser.email}
            </a>
          </p>
        ) : (
          <div className="chat__input__message">
            <input
              autoFocus
              placeholder="Write your message..."
              type="text"
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") handleOnSend()
              }}
            />
            <SendIcon className="chat__input__submit" onClick={handleOnSend} />
          </div>
        )}
      </div>
    </div>
  )
}

export default Chat
