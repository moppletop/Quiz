const sleep = time => new Promise(resolve => setTimeout(resolve, time));
const poll = (fn, time) => fn().then(sleep(time).then(() => poll(fn, time)))

let lastHashCode = null;
let originalNavBar = null;

function quizFeedbackLoop() {
    originalNavBar = document.getElementById('quiz-nav').innerHTML;

    let alertBox = document.getElementById('submit-alert');

    if (alertBox != null) {
        sleep(5000).then(value => alertBox.hidden = true);
    }

    poll(() => new Promise(() => pollQuizData().then(json => applyQuizData(json))), 1000);
}

const pollQuizData = async () => {
    const response = await fetch('/quiz/data/', {
        method: 'GET',
        // body: "",
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return response.json();
}

function applyQuizData(data) {
    if (lastHashCode != null && lastHashCode === data.hashCode) {
        return;
    }

    console.log(data);

    updateScores(data.htmlNav);
    updateQuizBody(data.htmlBody);

    lastHashCode = data.hashCode;
}

function updateQuizBody(html) {
    const quizData = document.getElementById('quiz-data');

    if (quizData == null) {
        return;
    }

    quizData.innerHTML = html;
}

function updateScores(html) {
    const navBar = document.getElementById('quiz-nav');

    if (navBar == null) {
        return;
    }

    navBar.innerHTML = originalNavBar + ' ' + html;
}
