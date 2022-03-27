const formatDate = (dateString) => {
  const options = { hour: "2-digit", minute: "2-digit" }
  return new Date(dateString).toLocaleTimeString(navigator.geolocation, options)
}

export { formatDate }
