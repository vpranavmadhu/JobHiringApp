import React, { useState } from 'react'
import './assets/Style.css'
import { useNavigate } from 'react-router-dom';

export default function AddJobPage() {

  const [companyId, setCompanyId] = useState('');
  const [title,setTitle] = useState('');
  const [description,setDescription] = useState('');
  const [skills, setSkills] = useState('');
  const [location, setLocation] = useState('');
  const [salary, setSalary] = useState('');
  const [exp, setExp] = useState('');
  const [jobType, setJobType] = useState();

  const navigate = useNavigate();

  const handleBack =() => {
    navigate("/admin");
}
  const handleSumit = async (e) => {
    e.preventDefault();

    const data = {
      companyId:companyId,
      title:title,
      description:description,
      skills: skills,
      location: location,
      salary: salary,
      exp: exp,
      jobType: jobType
    }
    console.log(data)

    const response = await fetch('http://localhost:9090/admin/add', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type' : 'application/json'},
      body: JSON.stringify(data),
    });

    if(response.ok) {
      alert("successfully added...!")
      navigate('/admin');
    } else {
      console.log("failed")
    }
  }

  return (
    <div className='admin-main'>
      <button className='go-back-btn' onClick={handleBack}>←</button>
      <div className='addjob-card'>
        <div className='card-content'>
          <h2>Add a New Post</h2>
          <form onSubmit={handleSumit}>
          <input type="text" placeholder='Company ID' value={companyId} onChange={(e)=> {setCompanyId(e.target.value)}}/>
            <input type="text" placeholder='title' value={title} onChange={(e)=> {setTitle(e.target.value)}}/>
            <textarea name="" placeholder='description' id="" value={description} onChange={(e)=> {setDescription(e.target.value)}} ></textarea>
            <input type="text" placeholder='Skills required' value={skills} onChange={(e)=> {setSkills(e.target.value)}}/>
            <input type="text" placeholder='location' value={location} onChange={(e)=> {setLocation(e.target.value)}} />
            <input type="text" placeholder='salary'value={salary} onChange={(e)=> {setSalary(e.target.value)}} />
            <input type="text" placeholder='experience Required' value={exp} onChange={(e)=> {setExp(e.target.value)}} />
            <div className='radio-group'>
              <div className='radio-options'>
                <label htmlFor="">FullTime:</label>
                <input type="radio" value="Full-time" checked={jobType === 'Full-time'} onChange={(e)=> {setJobType(e.target.value)}}/>
              </div>
              <div className='radio-options'>
                <label htmlFor="">PartTime:</label>
                <input type="radio" value="Part-time" checked={jobType === 'Part-time'} onChange={(e)=> {setJobType(e.target.value)}} />
              </div>
              <div className='radio-options'>
                <label htmlFor="">Contract:</label>
                <input type="radio" value="Contract" checked={jobType === 'Contract'} onChange={(e)=> {setJobType(e.target.value)}} />
              </div>
            </div>
            <button type='submit'>Submit Post</button>
          </form>
        </div>
      </div>
    </div>
  )
}
