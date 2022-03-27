import { useState } from "react"

import { getNewChatroom } from "../adapters/xhr"
import ClientChat from "../components/chat/ClientChat"
import Error from "../components/error/Error"
import { validateEmail } from "../utils/validation/emailValidation"
import "./Home.scss"

const SOCKET_URL = "http://localhost:8080/ws"

const Home = () => {
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [room, setRoom] = useState()
  const [error, setError] = useState()

  const connect = async () => {
    const res = await getNewChatroom(name, email)
    if (!res.error) {
      setRoom(res.data)
    }
  }

  const handleUserFormSubmit = (formName, formEmail) => {
    if (validateEmail(email)) {
      connect()
    } else {
      setError(
        "The format of the provided email is incorrect. Please enter a valid email."
      )
    }
  }

  const handleErrorClose = (e) => {
    e.stopPropagation()
    setError()
  }

  return (
    <div className="home">
      {error && <Error error={error} onErrorClose={handleErrorClose} />}
      {room ? (
        <>
          <ClientChat room={room} />
        </>
      ) : (
        <div className="userFormWrapper">
          <div className="userForm">
            <h1 className="userForm__title">Client support live chat</h1>
            <p className="userForm__description">
              Please enter your name and email in the form below to start
              chatting with our support service.
            </p>
            <label className="userForm__name">
              Name:
              <input
                autoFocus
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </label>
            <label className="userForm__email">
              Email:
              <input
                type="email"
                value={email}
                onKeyDown={(e) => {
                  if (e.key == "Enter") handleUserFormSubmit()
                }}
                onChange={(e) => setEmail(e.target.value)}
              />
            </label>
            <button className="userForm__submit" onClick={handleUserFormSubmit}>
              Connect
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default Home
