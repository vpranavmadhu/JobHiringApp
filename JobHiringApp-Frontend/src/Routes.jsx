import React from 'react'
import { Route, Routes, } from 'react-router-dom'
import JobPage from './JobList'
import JobApply from './JobApply'
import LoginPage from './LoginPage'
import AddJobPage from './AddJobPage'
import AdminPage from './AdminPage'
import RegisterPage from './RegisterPage'
import HomePage from './HomePage'
import VeiwJobsAdmin from './VeiwJobsAdmin'
import Profile from './Profile'


const AppRoutes = () => {
  return (
    <Routes>
      <Route path='/' element={<LoginPage/>}></Route>
      <Route path='/register' element={<RegisterPage />} />
      <Route path='/admin'element={<AdminPage />} />
      <Route path='/home' element={<HomePage />} />
      <Route path='/addjob' element={<AddJobPage />} />
      <Route path='/apply' element={<JobApply/>} />
      <Route path='/viewjob' element={<VeiwJobsAdmin/>} />
      <Route path='/profile' element={<Profile/>} />
    </Routes>
  )
}

export default AppRoutes

