function ShowAndHide() {
    var x = document.getElementById('SectionName');
    if (x.style.display === 'none') {
        x.style.display = 'block';
    } else {
        x.style.display = 'none';
    }
}



// ----------------- Script for dark- / light-mode changer --------------

// Grab the button & css
const btn = document.querySelector(".button-toggle-theme");
const theme = document.querySelector("#theme-link");

// Check for dark mode preference at the OS level, set to dark if true
// Checks also if light-mode has been enabled by the user
if (window.matchMedia("(prefers-color-scheme: dark)").matches & localStorage.getItem("csstheme") != "light") {
    theme.href = "resources/css/dark-theme.css";
    localStorage.setItem("csstheme", "dark");
} else if (localStorage.getItem("csstheme") == "light") {
    theme.href = "resources/css/light-theme.css";
    localStorage.setItem("csstheme", "light");
}

// Check for theme in local storage
const currentTheme = localStorage.getItem("csstheme");
// Apply current theme from local storage
if (currentTheme == "dark") {
    theme.href = "resources/css/dark-theme.css";
}

// Switch theme if user clicks on the "Toggle Theme" button
btn.addEventListener("click", function() {
    // Swap out the URL for the different stylesheets
    if (theme.getAttribute("href") == "resources/css/light-theme.css") {
        theme.href = "resources/css/dark-theme.css";
        localStorage.setItem("csstheme", "dark");
    } else {
        theme.href = "resources/css/light-theme.css";
        // Save the current preference to local storage
        localStorage.setItem("csstheme", "light");
    }
});