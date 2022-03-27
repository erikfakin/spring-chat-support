import { Link } from "react-router-dom"
import "./Header.scss"

const Header = () => {
  return (
    <div className="headerWrapper">
      <div className="header">
        <h3 className="header__logo">Chat Support</h3>
        <Link to="/dashboard" className="header__link">
          Dashboard
        </Link>
      </div>
    </div>
  )
}

export default Header
