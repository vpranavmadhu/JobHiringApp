import React, { useEffect, useState } from 'react'
import ProfileDropdown from './ProfileDropdown'
import { useNavigate } from 'react-router-dom';

export default function Header({ onSearchInput, showSearch, role }) {

  const [username, setUsername] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
      getUserDetails();
    }, []);
  

    const handlelogoclick = () => {
      navigate(-1);
    }

    const getUserDetails = async() => {
      try {

        const endpoint = role === "ADMIN" 
        ? 'http://localhost:9090/admin/get' 
        : 'http://localhost:9090/api/user/getUser';

        const response = await fetch(endpoint,
          {credentials:'include'}
        )
  
        const data = await response.json()
        if (data) {
          setUsername(data.username)
        }
        else {
          setUsername('Guest')
        }
      } catch(error) {
        console.log(error)
        setUsername('Guest')
      }
    }
    console.log("header rendering")
  return (
    <header className='header'>
      <div className='header-content' >
        <div className='header-logo' onClick={handlelogoclick}>
          <h3> " NeXT HIRE "</h3>
        </div>
        <div className='search-container'>
          {showSearch ? (<div><svg className="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path fill="#8C8594" d="M16.665 14.898h-.927l-.328-.317a7.618 7.618 0 0 0 1.736-6.26c-.551-3.26-3.273-5.863-6.558-6.262-4.962-.61-9.139 3.565-8.529 8.524.4 3.283 3.004 6.003 6.265 6.554a7.629 7.629 0 0 0 6.265-1.735l.316.328v.927l4.986 4.983a1.24 1.24 0 0 0 1.748 0c.481-.481.481-1.267 0-1.748l-4.974-4.994Zm-7.039 0a5.27 5.27 0 0 1-5.279-5.276 5.27 5.27 0 0 1 5.28-5.276 5.27 5.27 0 0 1 5.278 5.276 5.27 5.27 0 0 1-5.279 5.276Z" />
          </svg><input type="text" name="" id="" placeholder='Search jobs by title, company or skill' onChange={((e) => onSearchInput(e.target.value))} />
          </div>) : (<div>  </div>)}

        </div>
        <div className='header-profile'>
          <ProfileDropdown username={username} role={role}/>
        </div>
      </div>

    </header>
  )
}
