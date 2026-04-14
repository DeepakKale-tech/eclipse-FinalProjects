function ensureGlobalToast() {
    let container = document.getElementById("globalToastContainer");
    if (!container) {
        container = document.createElement("div");
        container.id = "globalToastContainer";
        container.className = "toast-container position-fixed top-0 end-0 p-3";
        container.style.zIndex = "1100";
        document.body.appendChild(container);
    }
    return container;
}

function showToast(message, type = "info", delay = 2200) {
    if (!window.bootstrap) {
        alert(message);
        return;
    }

    const typeClass = {
        success: "text-bg-success",
        error: "text-bg-danger",
        warning: "text-bg-warning",
        info: "text-bg-primary"
    }[type] || "text-bg-primary";

    const container = ensureGlobalToast();
    const toastEl = document.createElement("div");
    toastEl.className = `toast align-items-center border-0 ${typeClass}`;
    toastEl.setAttribute("role", "alert");
    toastEl.setAttribute("aria-live", "assertive");
    toastEl.setAttribute("aria-atomic", "true");
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;

    container.appendChild(toastEl);

    const toast = new bootstrap.Toast(toastEl, { delay });
    toast.show();

    toastEl.addEventListener("hidden.bs.toast", () => {
        toastEl.remove();
    });
}
