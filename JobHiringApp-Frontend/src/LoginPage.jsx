import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const handleSumit = async (e) => {
    e.preventDefault();
    setError(null);


    try {
      const response = await fetch('http://localhost:9090/api/user/login', {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        credentials: 'include',
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json();

      if (response.ok) {
        if (data.role === 'ADMIN') {
          navigate("/admin")
        } else if (data.role === 'SEEKER') {
          navigate("/home")
        } 
      } else {
          throw new Error(data.Error || 'Login failed')
        }
    } catch (error) {
      setError(error.message);
    }


  }
  return (
    <div className='login-main'>
      <div className='login-card'>
        <h1>LOGIN</h1>
        {error && <p className='error-message'>{error}</p>}
        <form onSubmit={handleSumit}>
          <input type="text" placeholder='Enter your username' value={username} onChange={(e) => { setUsername(e.target.value) }}  required/>
          <input type="text" placeholder='Enter yout password' value={password} onChange={(e) => { setPassword(e.target.value) }} required />
          <button type='submit'>Login </button>
        </form>
        <a href="/register">new user?Register Here</a>
      </div>
    </div>
  )
}
