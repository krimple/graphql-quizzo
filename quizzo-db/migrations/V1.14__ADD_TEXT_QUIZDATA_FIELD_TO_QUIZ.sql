alter table quizzo.quiz
    add column quizdata text null;

update quizzo.quiz
set quizdata = '{
	"id": 1,
	"title": "First Quiz",
	"description": "The first quiz I thought of",
	"questions": [{
		"id": 1,
		"text": "What is your name?",
		"options": [
			{
				"key": "A",
				"label": "Dumbledore",
				"score": 0
			},
			{
				"key": "B",
				"label": "Arthur, King of the Britains",
				"score": 100
			},
			{
				"key": "C",
				"label": "Jack in the Box",
				"score": 0
			}
		]
	},
	{
		"id": 2,
		"text": "What is your new name?",
		"options": [
			{
				"key": "A",
				"label": "Dumbledore",
				"score": 0
			},
			{
				"key": "B",
				"label": "Arthur, King of the Britains",
				"score": 0
			},
			{
				"key": "C",
				"label": "Jack in the Box",
				"score": 1000
			}
		]
	}
  ]
}
'
where id = (select min(id) from quizzo.quiz);

