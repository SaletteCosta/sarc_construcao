#!/bin/bash

echo "🌱 Populando banco de dados com dados de teste..."
echo ""

# Base URLs
ADMIN_URL="http://localhost:8084"
USER_URL="http://localhost:8085"

echo "📚 Criando Disciplinas..."
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "CS101", "name": "Introdução à Ciência da Computação"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "DS202", "name": "Estruturas de Dados"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "ALG303", "name": "Algoritmos Avançados"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "BD404", "name": "Banco de Dados"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "WEB505", "name": "Desenvolvimento Web"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "IA606", "name": "Inteligência Artificial"}' > /dev/null
curl -s -X POST $ADMIN_URL/subjects -H "Content-Type: application/json" -d '{"code": "SO707", "name": "Sistemas Operacionais"}' > /dev/null
echo "✅ 7 disciplinas criadas"

echo "👥 Criando Usuários..."
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "João Silva", "registration": "2024001", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Maria Santos", "registration": "2024002", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Pedro Oliveira", "registration": "2024003", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Ana Costa", "registration": "2024004", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Carlos Souza", "registration": "2024005", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Julia Lima", "registration": "2024006", "type": "STUDENT"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Prof. Carlos Mendes", "registration": "PROF001", "type": "TEACHER"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Prof. Fernanda Lima", "registration": "PROF002", "type": "TEACHER"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Prof. Roberto Silva", "registration": "PROF003", "type": "TEACHER"}' > /dev/null
curl -s -X POST $USER_URL/users -H "Content-Type: application/json" -d '{"name": "Admin Sistema", "registration": "ADMIN001", "type": "ADMIN"}' > /dev/null
echo "✅ 10 usuários criados (6 alunos, 3 professores, 1 admin)"

echo "🎓 Criando Turmas..."
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "CS101-A", "subjectId": 1, "schedule": "MON-08"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "CS101-B", "subjectId": 1, "schedule": "TUE-10"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "DS202-A", "subjectId": 2, "schedule": "WED-14"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "ALG303-A", "subjectId": 3, "schedule": "THU-08"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "BD404-A", "subjectId": 4, "schedule": "FRI-10"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "WEB505-A", "subjectId": 5, "schedule": "MON-14"}' > /dev/null
curl -s -X POST $ADMIN_URL/classes -H "Content-Type: application/json" -d '{"code": "IA606-A", "subjectId": 6, "schedule": "TUE-14"}' > /dev/null
echo "✅ 7 turmas criadas"

echo "🔗 Adicionando alunos às turmas..."
# CS101-A: João, Maria, Ana
curl -s -X POST $ADMIN_URL/classes/code/CS101-A/students/1 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/CS101-A/students/2 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/CS101-A/students/4 > /dev/null

# CS101-B: Pedro, Carlos
curl -s -X POST $ADMIN_URL/classes/code/CS101-B/students/3 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/CS101-B/students/5 > /dev/null

# DS202-A: João, Pedro, Julia
curl -s -X POST $ADMIN_URL/classes/code/DS202-A/students/1 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/DS202-A/students/3 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/DS202-A/students/6 > /dev/null

# ALG303-A: Maria, Carlos
curl -s -X POST $ADMIN_URL/classes/code/ALG303-A/students/2 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/ALG303-A/students/5 > /dev/null

# BD404-A: Ana, Julia
curl -s -X POST $ADMIN_URL/classes/code/BD404-A/students/4 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/BD404-A/students/6 > /dev/null

# WEB505-A: João, Maria, Pedro
curl -s -X POST $ADMIN_URL/classes/code/WEB505-A/students/1 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/WEB505-A/students/2 > /dev/null
curl -s -X POST $ADMIN_URL/classes/code/WEB505-A/students/3 > /dev/null

echo "✅ Alunos adicionados às turmas"

echo ""
echo "✅ Dados de teste criados com sucesso!"
echo ""
echo "📊 Resumo:"
echo "   - 7 Disciplinas"
echo "   - 7 Turmas"
echo "   - 10 Usuários (6 alunos, 3 professores, 1 admin)"
echo "   - 15+ Matrículas (alunos nas turmas)"
echo ""
echo "🌐 Acesse: http://localhost:3000"
echo ""
