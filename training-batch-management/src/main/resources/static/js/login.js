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

    if (!response.ok) {
        alert("Invalid credentials ❌");
        return;
    }

    const data = await response.json(); // ✅ IMPORTANT

    localStorage.setItem("token", data.token);

    const role = data.role;

    alert("Login successful ✅");

    if (role === "ADMIN") {
        window.location.href = "admin.html";
    } 
    else if (role === "TRAINER") {
        window.location.href = "trainer.html";
    }
    else if (role === "STUDENT") {
        window.location.href = "student.html";
    }
}