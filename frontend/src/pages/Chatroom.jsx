import { useState } from "react";

const Chatroom = () => {

    const [messages, setMessages] = useState([])
    const [chatrooms, setChatrooms] = useState([])

    const getMessages = async () => {
        const res = await fetch("http://localhost:8080/room/all")
        setChatrooms(await res.json())

    }

    return ( <div className="chatroom">
            
        {chatrooms.map(chatroom => <div className="chatroom">{chatroom.id}</div>)}
        <button onClick={() => {getMessages()}}>Get messages</button>
    </div > );
}

export default Chatroom;