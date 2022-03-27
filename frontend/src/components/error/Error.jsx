import "./Error.scss"

const Error = ({ error, onErrorClose }) => {
  return (
    <div className="errorWrapper" onClick={onErrorClose}>
      <div className="error">
        <div className="error__close" onClick={onErrorClose}>
          X
        </div>
        <div className="error__text">{error}</div>
      </div>
    </div>
  )
}

export default Error
