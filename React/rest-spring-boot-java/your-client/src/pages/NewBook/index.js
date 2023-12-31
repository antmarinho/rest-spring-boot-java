import React, {useState, useEffect} from "react";
import './styles.css'
import logoImage from '../../assets/logo.svg'
import { useNavigate, Link, useParams } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi"
import api from "../../services/api";


export default function NewBook(){

    const[id,setId] = useState(null);
    const[author,setAuthor] = useState('');
    const[launchDate,setLaunchDate] = useState('');
    const[price,setPrice] = useState('');
    const[title,setTitle] = useState('');

    const {bookId} = useParams();

    const username = localStorage.getItem('userName');
    const accessToken = localStorage.getItem('accessToken');

    const header = {headers: {Authorization: `Bearer ${accessToken}`}};

    const navigate = useNavigate();

    async function loadBook(){

        try {

                const Response = await api.get(`api/book/v1/${bookId}`, header);
            
                let ajustaData = Response.data.launchDate.split("T",10)[0];

                setId(Response.data.id)
                setAuthor(Response.data.author)
                setLaunchDate(ajustaData)
                setTitle(Response.data.title)
                setPrice(Response.data.price)

        } catch (error) {
            alert('error recovering book try again');
            navigate('/books');
        }

    }

    useEffect(() => {

        if (bookId === '0') 
            return;
        else 
            loadBook();

    }, {bookId})

    async function saveOrUpdate(e){

        e.preventDefault();

        const data = {title,author,launchDate,price};

        try {

                if (bookId === '0') {
                    
                    await api.post('api/book/v1',data, header);

                    navigate('/books');

                } else {

                    data.id = id;

                    await api.put('api/book/v1',data, header);

                    navigate('/books');
                    
                }
            
        } catch (error) {
            alert('Error while recording book! try again')
        }
    }

    return(
        <div className="new-book-container">
            <div className="content">
                <section className="form">
                    <img src={logoImage} alt="logoImage" />
                    <h1>{bookId === '0' ? 'Add New' : 'Update'} Book</h1>
                    <p>Enter the book information and click on {bookId === '0' ? "'Add'" : "'Update'"}!</p>
                    <Link className="back-link" to="/books">
                        <FiArrowLeft size={16} color="#251fc5"/>
                        Back to Book
                    </Link>
                </section>
                <form onSubmit={saveOrUpdate}>
                    <input
                        placeholder="Title"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                    />
                    <input
                        placeholder="Author"
                        value={author}
                        onChange={e => setAuthor(e.target.value)}
                    />
                    <input
                        type="date"
                        value={launchDate}
                        onChange={e => setLaunchDate(e.target.value)}
                    />
                    <input
                        placeholder="Price"
                        value={price}
                        onChange={e => setPrice(e.target.value)}
                    />

                    <button className="button" type="submit">{bookId === '0' ? 'Add' : 'Update'}</button>
                </form>
            </div>
        </div>
    );
}