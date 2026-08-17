export const EXERCISES = [
  {
    id: 1,
    title: 'Premier titre',
    description: 'Ajoute une balise <h1> avec du texte.',
    defaultCode: {
      html: '<h1>Bonjour !</h1>\n<p>Modifie ce code.</p>',
      css: 'h1 { color: #2563eb; font-family: sans-serif; }',
      js: "document.querySelector('h1').addEventListener('click', () => alert('Clic !'));",
    },
  },
];
