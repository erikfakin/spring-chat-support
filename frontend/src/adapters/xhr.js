import { API_ROOM_URL, API_URL } from "../configuration/configuration"

export const getNewChatroom = async (name, email) => {
  const res = await fetch(API_ROOM_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      name,
      email,
    }),
  })
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const getAllMessagesInChatroom = async (roomId) => {
  const res = await fetch(API_URL + roomId)
  if (res.ok) {
    return { data: await res.json() }
  } else return { error: res.status }
}

export const getNewMessagesClient = async (roomId) => {
  const res = await fetch(API_URL + "new/client/" + roomId)
  if (res.ok) {
    const newMessages = await res.json()
    if (newMessages.length === 0) return { data: [] }
    return { data: newMessages }
  }
  return { error: res.status }
}

export const getNewMessagesSupport = async (roomId) => {
  const res = await fetch(API_URL + "new/support/" + roomId)
  if (res.ok) {
    const newMessages = await res.json()
    if (newMessages.length === 0) return { data: [] }
    return { data: newMessages }
  }
  return
}

export const sendMessageClient = async (roomId, messageContent) => {
  const res = await fetch(API_URL + roomId + "?sender=client", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      content: messageContent,
    }),
  })
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const sendMessageSupport = async (roomId, messageContent) => {
  const res = await fetch(API_URL + roomId + "?sender=support", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      content: messageContent,
    }),
  })
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const getAllChatrooms = async () => {
  const res = await fetch(API_ROOM_URL + "all")
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const getChatrromById = async (chatroomId) => {
  const res = await fetch(API_ROOM_URL + chatroomId)
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const countNewMessagesByChatroom = async (chatroomId) => {
  const res = await fetch(API_URL + "new/support/" + chatroomId + "/count")

  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}
