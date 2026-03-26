async function login() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
    });

    const token = await response.text();

    // store token
    localStorage.setItem("token", token);

    alert("Login successful");

    // redirect (simple logic)
    if (email.includes("admin")) {
        window.location.href = "admin.html";
    } else {
        window.location.href = "trainer.html";
    }
}