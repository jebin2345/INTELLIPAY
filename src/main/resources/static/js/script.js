//global variables
var userObj = {};
var currencyEnum = {
    "ILS": "&#8362;",
    "USD": "&#36;",
    "EUR": "&#128;",
};

//global functions
function setUser(user) {
    userObj = user;
}

function setUsername() {
    document.getElementById("username_nav").innerText = userObj.name || (userObj.firstName + " " + userObj.lastName);
}

function navigate(show, hide) {
    document.getElementById(show).style = "display:block";
    document.getElementById(hide).style = "display:none";

    if (show === "login_page") {
        document.getElementById("main").style = "display:none";
    } else {
        setUsername();
        document.getElementById("main").style = "display:block";
    }
}

/*Login Page Functions */
function focusInChangeColor(e) {
    document.getElementById(e.id + "_div").classList.add("after-color");
}

function focusOutChangeColor(e) {
    document.getElementById(e.id + "_div").classList.remove("after-color");
}

function submitLogin() {
    
   const EMAIL = document.getElementById("email").value;
    const PASSWORD = document.getElementById("password").value;
    const ROLE = document.getElementById("role").value; // user or merchant
    const UPI_ID = document.getElementById("upi_id").value;

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({ "email": EMAIL, "password": PASSWORD, "upiId": UPI_ID });

    // Choose endpoint based on role
    var endpoint = ROLE === "merchant" ? "/merchantLogin" : "/Login";

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080" + endpoint, requestOptions)
        .then(response => {
            console.log("Response status:", response.status);
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Login failed with status: " + response.status);
            }
        })
        .then(result => {
            console.log("Login response:", result);
            if (result && result.trim() !== "") {
                setUser(JSON.parse(result));
                resetLogin();
                navigate("home_page", "login_page");
            } else {
                throw new Error("Empty response from server");
            }
        })
        .catch(error => {
            console.error("Login error:", error);
            handleError(error);
        });
}

function resetLogin() {
    document.getElementById("email").value = "";
    document.getElementById("password").value = "";
    document.getElementById("upi_id").value = "";
    document.getElementById("error-label-form").classList.remove("has-error");
}

function toggleUPIIdField() {
    const role = document.getElementById("role").value;
    const upiIdRow = document.getElementById("upi_id_row");
    
    if (role === "merchant") {
        upiIdRow.style.display = "block";
    } else {
        upiIdRow.style.display = "none";
    }
}

function handleError(error) {
    document.getElementById("error-label-form").classList.add("has-error");
}

function navigateHomePage() {
    document.getElementById("username_nav").value = userObj.name || (userObj.firstName + " " + userObj.lastName);
}

function setActiveTab(e) {
    let activeElemArr = document.body.getElementsByClassName("active");
    activeElemArr[0].classList.remove('active');
    e.classList.add('active');
}

function generateQR() {
    const amount = document.getElementById("price").value;
    
    if (!amount || amount <= 0) {
        alert("Please enter a valid amount");
        return;
    }
    
    if (!userObj.upiId) {
        alert("Please set your UPI ID first. Go back to login and enter your UPI ID.");
        return;
    }
    
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var raw = JSON.stringify({
        "merchantName": userObj.name,
        "merchantEmail": userObj.email,
        "upiId": userObj.upiId,
        "amount": amount,
        "currency": userObj.currency || "INR",
        "transactionNote": "Payment to " + userObj.name,
        "merchantAccountNumber": userObj.accountNumber,
        "merchantBankId": userObj.bankID
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/generateUPIQR", requestOptions)
        .then(response => response.blob())
        .then(blob => {
            var imageUrl = URL.createObjectURL(blob);
            document.getElementById("QR_image").src = imageUrl;
        })
        .catch(error => {
            console.log('error', error);
            alert('Error generating QR code. Please try again.');
        });
}

function getHistoryTransactions() {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify(userObj);

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/merchantHistory", requestOptions)
        .then(response => response.text())
        .then(result => createHistoryTable(JSON.parse(result)))
        .catch(error => console.log('error', error));
}

function createHistoryTable(transactions) {
    console.log(transactions);
    const tableBody = document.getElementById("history_body_table");
    resetTbody(tableBody);

    if (transactions && transactions.length > 0) {
        transactions.map(transaction => {
            var tr = document.createElement('tr');
            tr.innerHTML = `<tr>
                                <td>${transaction.merchantName}</td>
                                <td>${transaction.customerName || 'N/A'}</td>
                                <td>₹${transaction.amount}</td>
                                <td>${transaction.date} ${transaction.time}</td>
                                <td><span class="status-${transaction.status.toLowerCase()}">${transaction.status}</span></td>
                            </tr>`;
            tableBody.appendChild(tr);
        });
    } else {
        var tr = document.createElement('tr');
        tr.innerHTML = `<tr><td colspan="5" class="center">No transactions found</td></tr>`;
        tableBody.appendChild(tr);
    }
}

function resetTbody(tbody){
    for (var i = tbody.rows.length; i > 0; i--)
        tbody.deleteRow(i - 1);
}
