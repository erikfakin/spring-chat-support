import "./ChatroomList.scss"

const ChatroomList = ({ chatroom, onClick, activeChatroom }) => {
  return (
    <div
      className={`chatroomList ${
        chatroom.id === activeChatroom?.id ? "active" : ""
      }`}
      key={chatroom.id}
      onClick={() => {
        onClick(chatroom.id)
      }}
    >
      <h4 className="chatroomList__name">
        {chatroom.clientUser?.name}

        {chatroom.countNewMessages > 0 ? (
          <span className="chatroomList__count">
            {chatroom.countNewMessages}
          </span>
        ) : (
          ""
        )}
      </h4>

      <div
        className={`chatroomList__status ${
          chatroom.status == "ONLINE" ? "online" : "offline"
        }`}
      >
        {chatroom.status}
      </div>
    </div>
  )
}

export default ChatroomList
