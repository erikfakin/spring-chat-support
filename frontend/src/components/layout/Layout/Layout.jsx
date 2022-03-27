import Header from "../header/Header"
import "./Layout.scss"

const Layout = ({ children }) => {
  return (
    <>
      <Header />
      <div className="content">{children}</div>
    </>
  )
}

export default Layout
