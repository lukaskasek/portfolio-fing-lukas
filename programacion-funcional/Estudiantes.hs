{- Grupo: X
   Integrante(s):
     Guani, Joaquin, 5.334.276-4
     Kasek, Lukas, 5.446.964-2
-}

module Estudiantes where

import JSONLibrary
import TypedJSON
import Data.Maybe


---------------------------------------------------------------------------------------
-- Importante:
-- Notar que NO se puede importar el módulo AST, que es interno a la biblioteca.
---------------------------------------------------------------------------------------
--ejemplo para chequear si el tipo es igual al de otros estudiantes
estudianteEj :: JSON
estudianteEj =
  mkJObject
    [ ("apellido", mkJString "hola"),
     ("CI",       mkJNumber 6),
     ("nombre",   mkJString "hola"),
     ("cursos",
        mkJArray
          [ mkJObject
              [ ("nombre",   mkJString "John"),
               ("anio",     mkJNumber 3),
               ("codigo",   mkJNumber 3),
               ("nota",     mkJNumber 3),
               ("semestre", mkJNumber 3)
              ]
          ]
      )
    ]

curso :: Object JSON
curso =
  [ ("nombre",   mkJString "Jfohn")
  , ("anio",     mkJNumber 4)
  , ("codigo",   mkJNumber 5)
  , ("nota",     mkJNumber 6)
  , ("semestre", mkJNumber 6)
  ]



-- decide si un valor que representa un estudiante esta bien formado
-- aca puedo hacer uno de ejemplo, obtener su tipo, y chequear que sean iguales.
estaBienFormadoEstudiante :: JSON -> Bool
estaBienFormadoEstudiante a = case typeOf estudianteEj of
  Nothing -> False
  Just e -> if hasType a e then case lookupField a "cursos" of
                                  Nothing -> False
                                  Just cursos -> cursosOrdenados cursos
            else False

cursosOrdenados :: JSON -> Bool
cursosOrdenados jcursos = case fromJArray jcursos of
                            Just cursos -> cursosOrdenadosArreglo (3000, 3000, 9999) cursos
                            Nothing -> False

cursosOrdenadosArreglo :: (Integer, Integer, Integer) -> [JSON] -> Bool
cursosOrdenadosArreglo _ [] = True
cursosOrdenadosArreglo (a, s, c) (x:xs) = (esCursoAnterior (a, s, c) (anioSemCod x)) && 
                                          cursosOrdenadosArreglo (anioSemCod x) xs

esCursoAnterior :: (Integer, Integer, Integer) -> (Integer,Integer,Integer) -> Bool
esCursoAnterior (aa,ss,cc) (aaa, sss, ccc) = ((aa > aaa) || 
                                    ((aa == aaa) && ((ss > sss) || ((ss == sss) && (cc < ccc)))))

anioSemCod :: JSON -> (Integer, Integer, Integer)
anioSemCod job =
  let obj = fromMaybe [] (fromJObject job)
      a   = fromMaybe 0 (fromJNumber(fromMaybe (mkJNull ())(lookupFieldObj obj "anio")))
      s   = fromMaybe 0 (fromJNumber(fromMaybe (mkJNull ())(lookupFieldObj obj "semestre")))
      c   = fromMaybe 0 (fromJNumber(fromMaybe (mkJNull ())(lookupFieldObj obj "codigo")))
  in (a, s, c)

-- getters
getCI :: JSON -> Maybe Integer
getCI e = if estaBienFormadoEstudiante e then
            case lookupField e "CI" of
              Nothing -> Nothing
              Just j -> case (fromJNumber j) of
                Nothing -> Nothing
                Just s -> Just s
          else Nothing

getNombre :: JSON -> Maybe String
getNombre e = if estaBienFormadoEstudiante e then
            case lookupField e "nombre" of
              Nothing -> Nothing
              Just j -> case (fromJString j) of
                Nothing -> Nothing
                Just s -> Just s
          else Nothing

getApellido :: JSON -> Maybe String
getApellido e = if estaBienFormadoEstudiante e then
            case lookupField e "apellido" of
              Nothing -> Nothing
              Just j -> case (fromJString j) of
                Nothing -> Nothing
                Just s -> Just s
          else Nothing

getCursos :: JSON -> Maybe JSON
getCursos e = if estaBienFormadoEstudiante e then
            case lookupField e "cursos" of
              Nothing -> Nothing
              Just j -> Just j
          else Nothing

-- obtiene arreglo con cursos que fueron aprobados
aprobados :: JSON -> Maybe JSON
aprobados e =
  if not (estaBienFormadoEstudiante e) then Nothing
  else case getCursos e of
    Nothing -> Nothing
    Just cursosJson ->
      case fromJArray cursosJson of
        Nothing     -> Nothing
        Just cursos ->
          let cursosAprob =
                filter (\c ->
                          case lookupField c "nota" of
                            Just n ->
                              case fromJNumber n of
                                Just nn -> nn >= 3
                                _       -> False
                            _ -> False
                       ) cursos
              nuevoCursosJSON = mkJArray cursosAprob
              nuevoEstudiante = actualizarobjeto e "cursos" nuevoCursosJSON
          in Just nuevoEstudiante



-- obtiene arreglo con cursos rendidos en un año dado
enAnio :: Integer -> JSON -> Maybe JSON
enAnio anio e =
  if not (estaBienFormadoEstudiante e) then Nothing
  else case getCursos e of
    Nothing -> Nothing
    Just cursosJson ->
      case fromJArray cursosJson of
        Nothing -> Nothing
        Just cursos ->
          let filtrados = filter (\c ->
                                   case lookupField c "anio" of
                                     Just n ->
                                       case fromJNumber n of
                                         Just nn -> nn == anio
                                         _       -> False
                                     _ -> False
                                  ) cursos
          in Just (mkJArray filtrados)


-- retorna el promedio de las notas de los cursos
promedioEscolaridad :: JSON -> Maybe Float
promedioEscolaridad e = if estaBienFormadoEstudiante e then
                            case getCursos e of
                              Nothing ->Nothing
                              Just cc -> case fromJArray cc of
                                Nothing -> Nothing
                                Just a -> Just (promedio (map notaEnObjeto a))
                        else Nothing


notaEnObjeto :: JSON -> Float
notaEnObjeto j = case lookupField j "nota" of
                    Nothing -> -10000
                    Just n -> case fromJNumber n of
                        Nothing -> -100000
                        Just nn -> fromIntegral nn

promedio :: [Float] -> Float
promedio [] = -10000
promedio xs = (sum xs) / (cantidad xs)

cantidad :: [Float] -> Float
cantidad [] = 0
cantidad (x:xs) = 1 + cantidad xs




-- agrega curso a lista de cursos de un estudiante
addCurso :: Object JSON -> JSON -> JSON
addCurso nuevoCurso estudiante =
  case getCursos estudiante of
    Nothing -> estudiante
    Just a ->
      case fromJArray a of
        Nothing -> estudiante
        Just cursos ->
          let cursosActualizados = mkJArray (actualizarArray (mkJObject nuevoCurso) cursos)
          in  actualizarobjeto estudiante "cursos" cursosActualizados

actualizarArray :: JSON -> [JSON] -> [JSON]
actualizarArray j [] = j : []
actualizarArray j (x:xs) = if esCursoAnterior (anioSemCod j) (anioSemCod x) then 
                              (j : x : xs)
                            else (x : actualizarArray j xs) 