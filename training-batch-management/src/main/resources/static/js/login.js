async function login() {
    const loginBtn = document.getElementById("loginBtn");
    const localToastEl = document.getElementById("loginToast");
    const localToastBody = document.getElementById("loginToastBody");
    const localToast = bootstrap.Toast.getOrCreateInstance(localToastEl, { delay: 2200 });

    function notify(typeClass, text, tone = "info") {
        if (typeof showToast === "function") {
            showToast(text, tone);
            return;
        }
        localToastEl.classList.remove("text-bg-danger", "text-bg-success", "text-bg-info");
        localToastEl.classList.add(typeClass);
        localToastBody.textContent = text;
        localToast.show();
    }

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    loginBtn.disabled = true;
    loginBtn.textContent = "Signing in...";

    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        notify("text-bg-danger", "Invalid credentials. Please check email and password.", "error");
        loginBtn.disabled = false;
        loginBtn.textContent = "Login";
        return;
    }

    const data = await response.json(); // ✅ IMPORTANT

    localStorage.setItem("token", data.token);

    const role = data.role;
    notify("text-bg-success", "Login successful. Redirecting...", "success");

    setTimeout(() => {
        if (role === "ADMIN") {
            window.location.href = "admin.html";
        } 
        else if (role === "TRAINER") {
            window.location.href = "trainer.html";
        }
        else if (role === "STUDENT") {
            window.location.href = "student.html";
        } else {
            notify("text-bg-danger", "Unknown role received from server.", "error");
            loginBtn.disabled = false;
            loginBtn.textContent = "Login";
        }
    }, 500);
}