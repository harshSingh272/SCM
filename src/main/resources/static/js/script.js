document.addEventListener("DOMContentLoaded", () => {
    let currentTheme = getTheme();
    changeTheme();

    function changeTheme() {
        document.querySelector("html").classList.add(currentTheme);
        const changeThemeButton = document.querySelector("#theme_change_button");
        changeThemeButton.querySelector('span').textContent = currentTheme === 'light' ? 'Dark' : 'Light';
        changeThemeButton.addEventListener("click", () => {
            document.querySelector("html").classList.remove(currentTheme);
            currentTheme === 'dark' ? setTheme('light') : setTheme('dark');
            currentTheme = getTheme();
            document.querySelector("html").classList.add(currentTheme);
            changeThemeButton.querySelector('span').textContent = currentTheme === 'light' ? 'Dark' : 'Light';
    
        })
    }
    
    function setTheme(theme) {
        localStorage.setItem("theme", theme);
    }
    
    function getTheme() {
        let theme = localStorage.getItem("theme");
    
        return theme ? theme : "light";
    }
});

