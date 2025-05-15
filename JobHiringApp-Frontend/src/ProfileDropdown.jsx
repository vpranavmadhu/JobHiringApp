import React, { useState } from 'react'
import useravatar from './useravatar.png';
import { Link, useNavigate } from 'react-router-dom';
export default function ProfileDropdown({ username, role }) {

  const [drop, setDrop] = useState(false);
  const navigate = useNavigate();

  const handleLogout = async() => {
    try {
      const response = await fetch ('http://localhost:9090/api/user/logout',{
        method:'POST',
        credentials: 'include'
      });

      if(response.ok) {
        console.log("Logout successfull");
        navigate('/');
      } else {
        console.error('logout failed')
      }
    } catch (error) {
      console.error("error during logout: ", error);
    }
  }

  return (
    <div className='profile-dropdown'>
      <button className="profile-button" onClick={() => setDrop(!drop)}>
        <span >{username || 'Guest'}</span>
        <span className={`caret ${drop ? 'rotated' : ''}`}>⌄</span>
      </button>
      {drop && (
        <div className='dropdown-menu'>
          {role !== 'ADMIN' && (
            <Link to="/profile">Profile</Link>
          )}
          <Link onClick={handleLogout}>Logout</Link>
        </div>
      )}
    </div>
  )
}
