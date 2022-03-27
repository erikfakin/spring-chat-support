export const getNewChatroom = async (name, email) => {
  const res = await fetch(process.env.REACT_APP_BASE_URL + "/room", {
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
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/messages/" + roomId
  )
  if (res.ok) {
    return { data: await res.json() }
  } else return { error: res.status }
}

export const getNewMessagesClient = async (roomId) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/messages/new/client/" + roomId
  )
  if (res.ok) {
    const newMessages = await res.json()
    if (newMessages.length === 0) return { data: [] }
    return { data: newMessages }
  }
  return { error: res.status }
}

export const getNewMessagesSupport = async (roomId) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/messages/new/support/" + roomId
  )
  if (res.ok) {
    const newMessages = await res.json()
    if (newMessages.length === 0) return { data: [] }
    return { data: newMessages }
  }
  return
}

export const sendMessageClient = async (roomId, messageContent) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/messages/" + roomId + "?sender=client",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: messageContent,
      }),
    }
  )
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const sendMessageSupport = async (roomId, messageContent) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/messages/" + roomId + "?sender=support",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: messageContent,
      }),
    }
  )
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const getAllChatrooms = async () => {
  const res = await fetch(process.env.REACT_APP_BASE_URL + "/room/all")
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const getChatrromById = async (chatroomId) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL + "/room/" + chatroomId
  )
  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}

export const countNewMessagesByChatroom = async (chatroomId) => {
  const res = await fetch(
    process.env.REACT_APP_BASE_URL +
      "/messages/new/support/" +
      chatroomId +
      "/count"
  )

  if (res.ok) {
    return { data: await res.json() }
  }
  return { error: res.status }
}
