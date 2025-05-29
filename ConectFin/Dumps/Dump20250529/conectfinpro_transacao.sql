-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: conectfinpro
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `transacao`
--

DROP TABLE IF EXISTS `transacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transacao` (
  `id` int NOT NULL AUTO_INCREMENT,
  `valor` decimal(10,2) NOT NULL,
  `data` date NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  `categoria_id` int DEFAULT NULL,
  `usuario_cpf` varchar(11) DEFAULT NULL,
  `tipo` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `categoria_id` (`categoria_id`),
  KEY `transacao_ibfk_3_idx` (`usuario_cpf`),
  CONSTRAINT `fk_transacao_usuario_cpf` FOREIGN KEY (`usuario_cpf`) REFERENCES `usuario` (`CPF`),
  CONSTRAINT `transacao_ibfk_2` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transacao`
--

LOCK TABLES `transacao` WRITE;
/*!40000 ALTER TABLE `transacao` DISABLE KEYS */;
INSERT INTO `transacao` VALUES (1,1500.00,'2024-03-01','Salário',1,1,NULL,'E'),(2,350.00,'2024-03-02','Freelance',1,1,NULL,'E'),(3,1200.00,'2024-03-03','Aluguel',1,2,NULL,'S'),(4,450.00,'2024-03-04','Supermercado',1,2,NULL,'S'),(5,200.00,'2024-03-05','Conta de Luz',1,2,NULL,'S'),(6,150.00,'2024-03-06','Internet',1,2,NULL,'S'),(7,300.00,'2024-03-07','Bônus',1,1,NULL,'E'),(8,180.00,'2024-03-08','Combustível',1,2,NULL,'S'),(9,250.00,'2024-03-09','Lazer',1,2,NULL,'S'),(10,400.00,'2024-03-10','Investimento',1,1,NULL,'E'),(11,1500.00,'2024-03-01','Salário',2,1,NULL,'E'),(12,350.00,'2024-03-02','Freelance',2,1,NULL,'E'),(13,1200.00,'2024-03-03','Aluguel',2,2,NULL,'S'),(14,450.00,'2024-03-04','Supermercado',2,2,NULL,'S'),(15,200.00,'2024-03-05','Conta de Luz',2,2,NULL,'S'),(16,150.00,'2024-03-06','Internet',2,2,NULL,'S'),(17,300.00,'2024-03-07','Bônus',2,1,NULL,'E'),(18,180.00,'2024-03-08','Combustível',2,2,NULL,'S'),(19,250.00,'2024-03-09','Lazer',2,2,NULL,'S'),(20,400.00,'2024-03-10','Investimento',2,1,NULL,'E');
/*!40000 ALTER TABLE `transacao` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-29 17:39:11
