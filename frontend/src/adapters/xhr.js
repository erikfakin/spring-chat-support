import { API_URL } from "../configuration/configuration"

export const getNewMessagesClient = async (roomId) => {
    const res = await fetch(
        API_URL + "new/client/" + roomId
    )
    if (res.ok) {
        const newMessages = await res.json()
        if (newMessages.length === 0) return
        return newMessages
    }
}

export const getNewMessagesSupport = async () => {
    const res = await fetch(
        API_URL + "new/support/" + roomId
    )
    if (res.ok) {
        const newMessages = await res.json()
        if (newMessages.length === 0) return
        return newMessages
    }
}

export const sendMessageClient = async (roomId, messageContent) => {
    const res = await fetch(
        API_URL + roomId + "?sender=client",
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
        return await res.json()
    }

}

export const sendMessageSupport = async () => {
    const res = await fetch(
        API_URL + roomId + "?sender=support",
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
        return await res.json()
    }
}