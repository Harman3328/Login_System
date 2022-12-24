import './App.css';
import Header from './components/Header';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom'
import LoginPage from './components/LoginPage';
import Home from './components/Home';
import CreateAccount from './components/CreateAccount';
import LogoutPage from './components/LogoutPage';

function App() {
  return (
    <Router>
      <div>
        <Header />
      </div>
      <Routes>
        <Route path='/' element={<Home />} />
        <Route path='/login' element={<LoginPage />} />
        <Route path='/createaccount' element={<CreateAccount />} />
        <Route path='/logout' element={<LogoutPage />} />
      </Routes>
    </Router>
  );
}

export default App;
