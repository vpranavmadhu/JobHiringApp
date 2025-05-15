import React, { useEffect, useState } from 'react'
import './assets/Style.css'
import { useNavigate } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';

export default function AdminPage() {

  const navigate = useNavigate();
  const [username,setUsername] = useState('');
  
    useEffect(() => {
      getUserDetails();
    }, []);
  
    const getUserDetails = async() => {
      try {
        const response = await fetch('http://localhost:9090/admin/get',
          {credentials:'include'}
        )
  
        const data = await response.json()
        if (data) {
          setUsername(data.username)
        } else {
          setUsername('Guest')
        }
      } catch(error) {
        console.log(error)
        setUsername('Guest')
      }
    }

  const handleviewjob = () => {
    navigate('/viewjob')
  }
  const handlepostjob = () => {
    navigate('/addjob')
  }

  console.log("admin page render")

  return (
    <div className='admin-main'>
      <div className='home-header'>
        <Header name={username} showSearch={false} role={"ADMIN"}/>
      </div>
      <div className='home-content'>
        <div className='admin-container'>
          <div className='admin-sub' onClick={handlepostjob}><h2>Post New Job</h2></div>
          <div className='admin-sub' onClick={handleviewjob}><h2>Posted Jobs</h2></div>

        </div>
      </div>
      <div className='home-footer'>
        <Footer />
      </div>



    </div>
  )
}
