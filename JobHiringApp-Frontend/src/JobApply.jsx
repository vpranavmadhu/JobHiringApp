import React, { useEffect, useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom';
import './assets/Style.css'
import Header from './Header';
import Footer from './Footer';

export default function JobApply() {

  const location = useLocation();
  const { job } = location.state || {};
  if (!job) {
    return (
      <div className='home-main'>
        <Header showSearch={false} role={"SEEKER"} />
        <div className='home-content'>
          <p>No job data found. Please go back and select a job.</p>
        </div>
        <Footer />
      </div>
    );
  }

  const [company, setCompany] = useState(null);
  const cid = job.companyId;

  const navigate = useNavigate();

  useEffect(() => {
    getCompanyDetails(cid)
  }, []);

  const getCompanyDetails = async (cid) => {
    try {
      const response = await fetch(`http://localhost:9090/api/companies/get?cid=${cid}`, {
        credentials: 'include',
      });

      if (response.ok) {
        const data = await response.json();
        setCompany(data);
      } else {
        console.log("Failed to fetch company details");
      }
    } catch (error) {
      console.error("Error fetching company:", error);
    }
  };

  const jobId = job.id;
  const handleApply = async() => {

    try {
      const response = await fetch (`http://localhost:9090/api/user/apply?id=${jobId}`,{
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
      },
        credentials: 'include',
    });

    const data = await response.text();

    if(response.ok) {
      alert(data);
      navigate(-1);
    } else {
      console.error("Error:", data)
    }

    } catch (error) {
      console.error(error);
    }
  }

  const handleGoBack = () => {
    navigate(-1);  // Navigate back to the previous page
  };

  return (
    <div>
      <div className='home-main'>
        <div className='home-header'>
          <Header showSearch={false} role={"SEEKER"} />
        </div>
        <div className='home-content'>
          <div className='jobapply-main'>
  {company && (
    <div className='jobapply-company'>
      <h2>{company?.name || 'Company Name Not Available'}</h2>
      <h3>Visit: 
        <a href={company?.website || '#'} target="_blank" rel="noopener noreferrer">
          {company?.website || 'Website Not Available'}
        </a>
      </h3>
      <h3>HQ: {company?.location || 'Location Not Available'}</h3>
      <h3>Employees: {company?.employees || 'Employees info not available'}</h3>
    </div>
  )}

  <div className='jobapply-jd'>
    <h2>Job Description: </h2>
    <h3>Role: {job.title}</h3>
    <h3>Description:</h3>
    <p>{job.description}</p>
    <h3>Required Skills:</h3>
    <ol>
      {job.skillsRequired.map((skill, index) => (
        <li key={index}>{skill}</li>
      ))}
    </ol>
    <h3>Location: </h3><p>{job.location}</p>
    <h3>Experience Required: </h3><p>{job.experienceRequired}</p>
    <h3>Job Type:</h3>
    <p>{job.jobType}</p>
    <h3>Salary: </h3><p>{job.salary}</p>
    <button className="apply-button" onClick={handleApply}>Apply Now</button>
     <button className="go-back-button" onClick={handleGoBack}>Veiw another Jobs</button>
  </div>
</div>


        </div>
        <div className='home-footer'>
          <Footer />
        </div>
      </div>
    </div>
  )
}
