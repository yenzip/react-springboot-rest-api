import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, {useEffect, useState} from 'react';
import {ProductList} from "./components/ProductList";
import {Summary} from "./components/Summary";
import axios from "axios";

function App() {
    const [products, setProducts] = useState([
        {productId: 'uuid-1', productName: '콜롬비아 커피 1', category: '커피빈', price: 5000},
        {productId: 'uuid-2', productName: '콜롬비아 커피 2', category: '커피빈', price: 5000},
        {productId: 'uuid-3', productName: '콜롬비아 커피 3', category: '커피빈', price: 5000},
    ]);
    const [items, setItems] = useState([]);
    const [emailVerified, setEmailVerified] = useState(false); // 추가: 이메일 확인 상태

    const handleAddClicked = productId => {
        const product = products.find(v => v.productId === productId);
        const found = items.find(v => v.productId === productId);
        const updatedItems =
            found ? items.map(v => (v.productId === productId) ? {...v, count: v.count + 1} : v) : [...items, {
                ...product,
                count: 1
            }]
        setItems(updatedItems);
    }

    useEffect(() => {
        axios.get('http://localhost:8080/api/v1/products')
            .then(v => setProducts(v.data));
    }, []);

    const handleCheckEmail = (email) => {
        if (email === "") {
            alert("이메일을 입력해주세요.");
        } else {
            axios.get(`http://localhost:8080/api/v1/members?email=${email}`)
                .then(response => {
                    setEmailVerified(true);
                    alert("회원 인증되었습니다.");
                })
                .catch(error => {
                    if (error.response.status === 400) {
                        setEmailVerified(false); // 이메일 확인 실패
                        alert("해당 이메일로 가입된 회원이 없습니다.");
                    } else {
                        console.error("이메일 확인 중 오류 발생:", error);
                    }
                });
        }
    }

    const handleOrderSubmit = (order) => {
        if (items.length === 0) {
            alert("아이템을 추가해 주세요!");
        } else if (!emailVerified) {
            alert("회원만 구매가 가능합니다. 회원 가입을 먼저 진행해주세요.");
        } else {
            axios.post('http://localhost:8080/api/v1/orders', {
                email: order.email,
                address: order.address,
                postcode: order.postcode,
                orderItems: items.map(v => ({
                    productId: v.productId,
                    category: v.category,
                    price: v.price,
                    quantity: v.count
                }))
            }).then(
                v => alert("주문이 정상적으로 접수되었습니다."),
                e => {
                    alert("서버 장애");
                    console.error(e);
                })
        }
    }

    return (
        <div className="container-fluid">
            <div className="row justify-content-center m-4">
                <h1 className="text-center">STARBOOKS</h1>
            </div>
            <div className="card">
                <div className="row">
                    <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                        <ProductList products={products} onAddClick={handleAddClicked}/>
                    </div>
                    <div className="col-md-4 summary p-4">
                        <Summary items={items} onOrderSubmit={handleOrderSubmit} onCheckEmail={handleCheckEmail}/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default App;
