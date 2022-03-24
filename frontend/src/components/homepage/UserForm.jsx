import { useState } from "react";

const UserForm = (onSubmit) => {
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")

    return ( 
        <div className="userForm">
          <label className="userForm__name">
            Name:
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </label>
          <label className="userForm__email">
            Email:
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </label>
          <button onClick={onSubmit}>Connect</button>
        </div>

     );
}
 
export default UserForm;