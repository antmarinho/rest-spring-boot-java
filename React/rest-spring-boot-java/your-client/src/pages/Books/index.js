import React, {useState, useEffect} from "react";
import { Link, useNavigate } from "react-router-dom";
import { FiPower, FiEdit, FiTrash2 } from "react-icons/fi"
import api from "../../services/api";
import './styles.css';
import logoImage from '../../assets/logo.svg'

export default function Books(){

const [books,setBooks] = useState([]);

    const username = localStorage.getItem('userName');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();

    const header = {headers: {Authorization: `Bearer ${accessToken}`}};
    const para = {params: {page: 1, limit: 4, direction: 'asc'}};

    async function logout(){
        localStorage.clear();

        navigate('/');
    }

    async function editBook(id){

        try {
            
                navigate(`/book/new/${id}`);

        } catch (error) {
            alert('Edit failed try again');
        }

    }

    async function deleteBook(id){

        try {
            
                await api.delete(`api/book/v1/${id}`,header);

                setBooks(books.filter(book => book.id !== id));

        } catch (error) {
            alert('Delete failed try again');
        }

    }

    useEffect(() => {api.get('api/book/v1', header,para).then(Response => setBooks(Response.data))}, []);

    return (
        <div className="book-container">
            <header>
                <img src={logoImage} alt="logo"/>
                <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
                <Link className="button" to="/book/new/0">Add New Book</Link>
                <button onClick={logout} type="button">
                    <FiPower size={18} color="#251FC5"/>
                </button>
            </header>

            <h1>Registered Books</h1>
            <ul>
                {books.map(book => (
                    <li key={book.id}>
                        <strong>Title:</strong>
                        <p>{book.title}</p>
                        <strong>Author:</strong>
                        <p>{book.author}</p>
                        <strong>Price:</strong>
                        <p>{Intl.NumberFormat('pt-BR',{style: 'currency', currency: 'BRL'}).format(book.price)}</p>
                        <strong>Release Date:</strong>
                        <p>{Intl.DateTimeFormat('pt-BR').format(new Date(book.launchDate))}</p>
              
                        <button onClick={() => editBook(book.id)} type="button">
                            <FiEdit size={20} color="#251FC5" />
                        </button>
              
                        <button onClick={() => deleteBook(book.id)} type="button">
                            <FiTrash2 size={20} color="#251FC5" />
                        </button>
                    </li>
                ))};
            </ul>
        </div>
    );
    
}