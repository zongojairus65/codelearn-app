package com.codelearn.api.config;

import com.codelearn.api.model.Chapter;
import com.codelearn.api.model.Exercise;
import com.codelearn.api.model.Lesson;
import com.codelearn.api.repository.ChapterRepository;
import com.codelearn.api.repository.ExerciseRepository;
import com.codelearn.api.repository.LessonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;

    public DataInitializer(ChapterRepository chapterRepository,
                            LessonRepository lessonRepository,
                            ExerciseRepository exerciseRepository) {
        this.chapterRepository = chapterRepository;
        this.lessonRepository = lessonRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {
        if (chapterRepository.count() > 0) {
            return;
        }

        Chapter chapter1 = new Chapter();
        chapter1.setTitle("HTML - Les bases");
        chapter1.setDescription("Apprends les fondamentaux du HTML : titres, textes, liens, images, listes, tableaux, formulaires et structure sémantique.");
        chapter1.setOrderIndex(1);
        chapter1.setStatus(Chapter.ChapterStatus.PRELOADED);
        chapterRepository.save(chapter1);

        createLesson(chapter1, 1, "Ton premier titre",
                "Un titre principal se crée avec la balise <h1>. Elle indique le sujet le plus important de ta page.",
                "Premier titre", "Ajoute une balise <h1> avec du texte.",
                "<h1>Salut</h1>", "h1 { color: #2563eb; font-family: sans-serif; }",
                "document.querySelector('h1').addEventListener('click', () => alert('Clic !'));",
                "{\"requiredTags\": [\"h1\"], \"minTextLength\": 1}");

        createLesson(chapter1, 2, "Les niveaux de titres",
                "HTML propose 6 niveaux de titres, de <h1> (le plus important) à <h6> (le moins important).",
                "Ajoute un sous-titre", "Sous le <h1> existant, ajoute un <h2> avec du texte.",
                "<h1>Mon site</h1>\n<h2>Bienvenue</h2>", "h1 { color: #2563eb; } h2 { color: #64748b; }", "",
                "{\"requiredTags\": [\"h1\", \"h2\"], \"minTextLength\": 1}");

        createLesson(chapter1, 3, "Écrire un paragraphe",
                "La balise <p> sert à écrire un paragraphe de texte normal.",
                "Ton premier paragraphe", "Ajoute une balise <p> contenant une phrase.",
                "<h1>Mon blog</h1>\n<p>Ceci est mon premier article.</p>", "body { font-family: sans-serif; }", "",
                "{\"requiredTags\": [\"p\"], \"minTextLength\": 5}");

        createLesson(chapter1, 4, "Mettre en valeur du texte",
                "La balise <strong> met un texte en gras. La balise <em> met un texte en italique.",
                "Mets un mot en valeur", "Entoure un mot avec <strong> pour le mettre en gras.",
                "<p>Ce cours est <strong>important</strong> pour progresser.</p>", "", "",
                "{\"requiredTags\": [\"strong\"], \"minTextLength\": 1}");

        createLesson(chapter1, 5, "Sauts de ligne et séparateurs",
                "La balise <br> insère un saut de ligne. La balise <hr> trace une ligne horizontale.",
                "Sépare deux lignes", "Ajoute un <br> entre les deux lignes de texte.",
                "<p>Première ligne<br>Deuxième ligne</p>", "", "",
                "{\"requiredTags\": [\"br\"], \"minTextLength\": 1}");

        createLesson(chapter1, 6, "Créer des liens",
                "La balise <a> avec l'attribut href crée un lien cliquable vers une autre page.",
                "Ton premier lien", "Ajoute un lien <a> qui pointe vers 'https://example.com'.",
                "<p>Visite <a href=\"https://example.com\">ce site</a> pour en savoir plus.</p>", "a { color: #2563eb; }", "",
                "{\"requiredTags\": [\"a\"], \"minTextLength\": 1}");

        createLesson(chapter1, 7, "Insérer une image",
                "La balise <img> affiche une image, grâce à l'attribut src (le chemin) et alt (le texte alternatif).",
                "Ajoute une image", "Ajoute une balise <img> avec un attribut src et un attribut alt.",
                "<img src=\"https://placehold.co/150\" alt=\"Illustration\">", "img { border-radius: 8px; }", "",
                "{\"requiredTags\": [\"img\"], \"minTextLength\": 0}");

        createLesson(chapter1, 8, "Listes à puces",
                "La balise <ul> crée une liste à puces, et chaque élément est un <li>.",
                "Fais ta liste de courses", "Crée une <ul> contenant au moins 2 éléments <li>.",
                "<ul>\n  <li>Pain</li>\n  <li>Lait</li>\n</ul>", "", "",
                "{\"requiredTags\": [\"ul\", \"li\"], \"minTextLength\": 1}");

        createLesson(chapter1, 9, "Listes numérotées",
                "La balise <ol> crée une liste numérotée, avec des <li> comme pour les listes à puces.",
                "Étapes numérotées", "Crée une <ol> avec au moins 2 étapes.",
                "<ol>\n  <li>Ouvrir le livre</li>\n  <li>Lire la page 1</li>\n</ol>", "", "",
                "{\"requiredTags\": [\"ol\", \"li\"], \"minTextLength\": 1}");

        createLesson(chapter1, 10, "Structurer avec des divisions",
                "La balise <div> regroupe des éléments ensemble pour organiser la page en blocs.",
                "Crée un bloc", "Regroupe un titre et un paragraphe dans une <div>.",
                "<div>\n  <h2>Section</h2>\n  <p>Contenu de la section.</p>\n</div>", "div { padding: 10px; border: 1px solid #ccc; }", "",
                "{\"requiredTags\": [\"div\"], \"minTextLength\": 1}");

        createLesson(chapter1, 11, "Texte en ligne avec span",
                "La balise <span> permet de cibler une portion de texte à l'intérieur d'une ligne, souvent pour la styliser.",
                "Colore un mot", "Entoure un mot avec <span> pour pouvoir le styliser.",
                "<p>Ce mot est <span>spécial</span>.</p>", "span { color: #e11d48; font-weight: bold; }", "",
                "{\"requiredTags\": [\"span\"], \"minTextLength\": 1}");

        createLesson(chapter1, 12, "Créer un tableau simple",
                "La balise <table> crée un tableau, <tr> une ligne, et <td> une cellule.",
                "Ton premier tableau", "Crée un tableau avec au moins une ligne <tr> et deux cellules <td>.",
                "<table>\n  <tr>\n    <td>Nom</td>\n    <td>Âge</td>\n  </tr>\n</table>", "table { border-collapse: collapse; } td { border: 1px solid #ccc; padding: 4px; }", "",
                "{\"requiredTags\": [\"table\", \"tr\", \"td\"], \"minTextLength\": 1}");

        createLesson(chapter1, 13, "En-têtes de tableau",
                "La balise <th> définit une cellule d'en-tête. <thead> et <tbody> structurent le tableau en sections.",
                "Ajoute un en-tête", "Ajoute une ligne <th> avant les données du tableau.",
                "<table>\n  <thead>\n    <tr><th>Nom</th><th>Âge</th></tr>\n  </thead>\n  <tbody>\n    <tr><td>Alex</td><td>20</td></tr>\n  </tbody>\n</table>", "table { border-collapse: collapse; } th, td { border: 1px solid #ccc; padding: 4px; }", "",
                "{\"requiredTags\": [\"th\", \"thead\", \"tbody\"], \"minTextLength\": 1}");

        createLesson(chapter1, 14, "Formulaires : champ texte",
                "La balise <form> crée un formulaire, et <input> permet de saisir du texte.",
                "Ton premier champ", "Crée un <form> avec un <input> de type texte.",
                "<form>\n  <input type=\"text\" placeholder=\"Ton nom\">\n</form>", "input { padding: 6px; }", "",
                "{\"requiredTags\": [\"form\", \"input\"], \"minTextLength\": 0}");

        createLesson(chapter1, 15, "Boutons et validation",
                "La balise <button> crée un bouton cliquable. Avec type='submit', il valide un formulaire.",
                "Ajoute un bouton d'envoi", "Ajoute un <button> de type submit dans le formulaire.",
                "<form>\n  <input type=\"text\">\n  <button type=\"submit\">Envoyer</button>\n</form>", "button { background: #2563eb; color: white; border: none; padding: 6px 12px; }", "",
                "{\"requiredTags\": [\"button\"], \"minTextLength\": 1}");

        createLesson(chapter1, 16, "Cases à cocher et boutons radio",
                "input type='checkbox' crée une case à cocher, input type='radio' un bouton radio (choix unique).",
                "Ajoute une case à cocher", "Ajoute un <input type=\"checkbox\"> avec un label associé.",
                "<label><input type=\"checkbox\"> J'accepte les conditions</label>", "", "",
                "{\"requiredTags\": [\"input\"], \"minTextLength\": 0}");

        createLesson(chapter1, 17, "Structure sémantique",
                "<header> définit l'en-tête d'une page, <footer> son pied de page.",
                "Ajoute un header et un footer", "Structure la page avec un <header> et un <footer>.",
                "<header><h1>Mon site</h1></header>\n<p>Contenu principal</p>\n<footer><p>Contact : moi@mail.com</p></footer>", "", "",
                "{\"requiredTags\": [\"header\", \"footer\"], \"minTextLength\": 1}");

        createLesson(chapter1, 18, "Sémantique avancée",
                "<nav> regroupe les liens de navigation, <main> le contenu principal, <section> une section thématique.",
                "Structure la navigation", "Ajoute un <nav> avec des liens et un <main> pour le contenu.",
                "<nav><a href=\"#\">Accueil</a> <a href=\"#\">Contact</a></nav>\n<main><section><h2>Bienvenue</h2></section></main>", "", "",
                "{\"requiredTags\": [\"nav\", \"main\", \"section\"], \"minTextLength\": 1}");

        createLesson(chapter1, 19, "Article et contenu autonome",
                "<article> représente un contenu autonome (comme un billet de blog). <aside> représente un contenu secondaire (comme une barre latérale).",
                "Crée un article", "Ajoute un <article> avec un titre et un texte, et un <aside> à côté.",
                "<article>\n  <h2>Mon article</h2>\n  <p>Le contenu de l'article.</p>\n</article>\n<aside><p>Liens utiles</p></aside>", "", "",
                "{\"requiredTags\": [\"article\", \"aside\"], \"minTextLength\": 1}");

        createLesson(chapter1, 20, "Projet de synthèse",
                "C'est l'heure de combiner tout ce que tu as appris : titres, textes, liens, images, listes, structure sémantique.",
                "Construis ta page complète", "Crée une page avec un <header>, un <h1>, un <p>, une <ul>, et un <footer>.",
                "<header><h1>Mon portfolio</h1></header>\n<main>\n  <p>Bienvenue sur mon site.</p>\n  <ul>\n    <li>Projet 1</li>\n    <li>Projet 2</li>\n  </ul>\n</main>\n<footer><p>© 2026</p></footer>",
                "body { font-family: sans-serif; }", "",
                "{\"requiredTags\": [\"header\", \"h1\", \"p\", \"ul\", \"footer\"], \"minTextLength\": 1}");
    }

    private void createLesson(Chapter chapter, int order, String lessonTitle, String theory,
                               String exTitle, String instructions, String starterHtml,
                               String starterCss, String starterJs, String validationRules) {
        Lesson lesson = new Lesson();
        lesson.setChapter(chapter);
        lesson.setTitle(lessonTitle);
        lesson.setTheoryContent(theory);
        lesson.setOrderIndex(order);
        lesson.setXpReward(20);
        lessonRepository.save(lesson);

        Exercise ex = new Exercise();
        ex.setLesson(lesson);
        ex.setTitle(exTitle);
        ex.setInstructions(instructions);
        ex.setStarterHtml(starterHtml);
        ex.setStarterCss(starterCss);
        ex.setStarterJs(starterJs);
        ex.setValidationRules(validationRules);
        ex.setOrderIndex(1);
        ex.setXpReward(15);
        exerciseRepository.save(ex);
    }
}
