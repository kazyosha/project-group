console.log("[reload.js] File loaded ✅");

document.addEventListener("DOMContentLoaded", function () {
    console.log("[reload.js] DOMContentLoaded triggered");

    function initReloadNavigation() {
        const sidebar = document.querySelector(".sidebar");
        const contentWrapper = document.getElementById("content-wrapper");

        if (!sidebar || !contentWrapper) return false;

        const sidebarLinks = sidebar.querySelectorAll("a");
        sidebarLinks.forEach(link => {
            link.addEventListener("click", function (event) {
                const href = this.getAttribute("href");
                if (!href || href.includes("/logout")) return;

                event.preventDefault();
                contentWrapper.innerHTML = `
                    <div class="text-center py-5">
                        <div class="spinner-border text-primary" role="status"></div>
                        <p class="mt-3">Đang tải nội dung...</p>
                    </div>`;

                fetch(href, {
                    headers: {
                        "X-Requested-With": "XMLHttpRequest",
                        credentials: "same-origin"
                    }
                })
                    .then(r => r.text())
                    .then(html => {
                        const parser = new DOMParser();
                        const doc = parser.parseFromString(html, "text/html");
                        const newContent =
                            doc.querySelector("section[layout\\:fragment='content']") ||
                            doc.querySelector("section");

                        if (newContent) contentWrapper.innerHTML = newContent.innerHTML;

                        history.pushState(null, "", href);

                        if (typeof initMyFeatures === "function") initMyFeatures();
                        if (typeof initBorrowList === "function") initBorrowList();
                    })
                    .catch(err => {
                        console.error(err);
                        contentWrapper.innerHTML = `
                            <div class="alert alert-danger text-center mt-5">
                                ❌ Lỗi khi tải nội dung. Vui lòng thử lại.
                            </div>`;
                    });
            });
        });

        // Back/forward
        window.addEventListener("popstate", () => {
            fetch(location.pathname, {headers: {"X-Requested-With": "XMLHttpRequest"}})
                .then(r => r.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, "text/html");
                    const newContent =
                        doc.querySelector("section[layout\\:fragment='content']") ||
                        doc.querySelector("section");
                    if (newContent) {
                        contentWrapper.innerHTML = newContent.innerHTML;
                        if (typeof initMyFeatures === "function") initMyFeatures();
                        if (typeof initBorrowList === "function") initBorrowList();
                    }
                });
        });

        return true;
    }

    let attempts = 0;
    const interval = setInterval(() => {
        attempts++;
        const ok = initReloadNavigation();
        if (ok || attempts >= 20) clearInterval(interval);
    }, 300);
});
