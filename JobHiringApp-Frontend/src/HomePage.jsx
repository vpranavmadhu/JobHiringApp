import React, { useState,useEffect, use } from 'react'
import Header from './Header'
import Footer from './Footer'
import './assets/Style.css'
import JobList from './JobList'

export default function HomePage() {
  // const [username,setUsername] = useState('');
  const [title,setTitle] = useState('');

  const handleSearchInput = (value) => {
    setTitle(value);
  }

  // useEffect(() => {
  //   getUserDetails();
  // }, [/*username*/]);

  // const getUserDetails = async() => {
  //   try {
  //     const response = await fetch('http://localhost:9090/api/user/getUser',
  //       {credentials:'include'}
  //     )

  //     const data = await response.json()
  //     if (data) {
  //       setUsername(data.username)
  //     } else {
  //       setUsername('Guest')
  //     }
  //   } catch(error) {
  //     console.log(error)
  //     setUsername('Guest')
  //   }
  // }
    console.log("home page render")
  return (
    <div className='home-main'>
      <div className='home-header'>
        <Header showSearch={true} onSearchInput={handleSearchInput} role={"SEEKER"}/>
      </div>
      <div className='home-content'>
        <JobList title={title} />
      </div>
      <div className='home-footer'>
        <Footer/>
      </div>
    </div>
  )
}
