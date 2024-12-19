import videoHomePage from '../../assets/video-homepage.mp4';

const HomePage = (props) => {
    return (
        <div className="homepage-container">
            <video loop muted autoPlay>
                <source src={videoHomePage} type="video/mp4" />
            </video>
            <div className='homepage-content'>
                <h1 className='hero-title'>Get to know your customers with forms worth filling out</h1>
                <p className="hero-body">
                    <span>Collect all the data you need to <strong>understand customers</strong> with forms designed to be refreshingly different.
                    </span>
                </p>
                <div className='hero-btn'>
                    <button className='btn-get'>Get started-it's free</button>
                </div>
            </div>
        </div>
    )
}

export default HomePage;