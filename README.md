<p align="center">
<img src="https://raw.githubusercontent.com/lucasvenez/RQL2SQL/images/rql-logo.png"/>
</p>

## Relational Query Language

### Introduction

Relational Query Language (RQL) is a query language created by Lucas Venezian Povoa [1] based on the Original Relational Algebra by E.F. Codd and the A Relational Algebra by C.J. Date and H. Darwen. It aims at defining an actual abstract and mathematical-based query language for the actual Relational Model defined in [2].

RQL2SQL is a Java framework developed by Dérick Welman and Lucas Venezian Povoa [3] that aims at translating Relational Query Language [1] sentences into Structured Query Language.

### Exemples

Considering the following conceptual data model.

```
Sale(idSale, date, time, idClient) -[1:n]- SaleItem(idSale, idProduct) -[n:1]- Product(idProduct, name)
```

The question "Which are the Products related to all Sales?" could be answer in SQL as: 

```SQL
SELECT DISTINCT idProduct
FROM SaleItem
WHERE 
    (
	SELECT COUNT(idSale)
	FROM Sale
    ) = 
    (
	SELECT COUNT(*)
	FROM SaleItem AS si
	WHERE si.idProduct = SaleItem.idProduct
    )
```

and in RQL as:

```SQL
¢ idProduct, idSale (SaleItem) / ¢ idSale (Sale)
```
## References

[1] Lucas Venezian Povoa. Relational Query Language: a purely relational query language (portuguese). Graduation Thesis. Supervisor: João Maurício Hipólito. 2011.

[2] [C.J. Date. Introduction to Database Systems, 8th edition. 2003. ISBN: 978-0321197849.](http://dl.acm.org/citation.cfm?id=861613&CFID=615788245&CFTOKEN=38751876)

[3] Dérick Welman. Towards a Purely Relational Query Language: comparison between the structured and relational query languages (portuguese). Graduation Thesis. Supervisor: Lucas Venezian Povoa. 2016.